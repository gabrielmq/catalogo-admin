package com.fullcycle.catalogo.admin.infrastructure.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.catalogo.admin.ControllerTest;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryCommand;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryOutput;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.DomainException;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CreateCategoryAPIInput;
import io.vavr.API;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput =
                new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
            .thenReturn(Right(CreateCategoryOutput.from(CategoryID.from("123").getValue())));

        final var request = post("/categories")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        mvc.perform(request)
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/categories/123"))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1))
            .execute(argThat(cmd ->
                    Objects.equals(expectedName, cmd.name())
                    && Objects.equals(expectedDescription, cmd.description())
                    && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var request = post("/categories")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aInput =
                new CreateCategoryAPIInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
            .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        final var request = post("/categories")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aInput));

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1))
                .execute(argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                                && Objects.equals(expectedDescription, cmd.description())
                                && Objects.equals(expectedIsActive, cmd.isActive())
                ));
    }
}
