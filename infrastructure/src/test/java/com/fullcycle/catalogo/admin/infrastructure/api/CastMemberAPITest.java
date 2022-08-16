package com.fullcycle.catalogo.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.catalogo.admin.ControllerTest;
import com.fullcycle.catalogo.admin.Fixture;
import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberOutput;
import com.fullcycle.catalogo.admin.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.get.DefaultGetCastMemberByIdUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.get.GetCastMemberByIdUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.list.DefaultListCastMembersUseCase;
import com.fullcycle.catalogo.admin.application.castmember.retrive.list.ListCastMembersUseCase;
import com.fullcycle.catalogo.admin.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.fullcycle.catalogo.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private DefaultCreateCastMemberUseCase createCastMemberUseCase;

    @MockBean
    private DefaultDeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockBean
    private DefaultGetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @MockBean
    private DefaultListCastMembersUseCase listCastMembersUseCase;

    @MockBean
    private DefaultUpdateCastMemberUseCase updateCastMemberUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCastMember_thenShouldReturnItsIdentifier() throws Exception {
        // given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var expectedId = CastMemberID.from("123");

        final var aCommand =
            new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
            .thenReturn(CreateCastMemberOutput.from(expectedId.getValue()));

        // when
        final var aRequest = post("/cast_members")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = mvc.perform(aRequest).andDo(print());

        // then
        response
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/cast_members/" + expectedId.getValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        verify(createCastMemberUseCase)
            .execute(argThat(actualCmd ->
                Objects.equals(expectedName, actualCmd.name())
                && Objects.equals(expectedType, actualCmd.type())
            ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateCastMember_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedType = Fixture.CastMember.type();
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand =
            new CreateCastMemberRequest(expectedName, expectedType);

        when(createCastMemberUseCase.execute(any()))
            .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        // when
        final var aRequest = post("/cast_members")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = mvc.perform(aRequest).andDo(print());

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCastMemberUseCase)
            .execute(argThat(actualCmd ->
                Objects.equals(expectedName, actualCmd.name())
                && Objects.equals(expectedType, actualCmd.type())
            ));
    }
}
