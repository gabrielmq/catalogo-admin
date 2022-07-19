package com.fullcycle.catalogo.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.catalogo.admin.ControllerTest;
import com.fullcycle.catalogo.admin.application.genre.create.CreateGenreOutput;
import com.fullcycle.catalogo.admin.application.genre.create.CreateGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.catalogo.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.catalogo.admin.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.catalogo.admin.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.update.UpdateGenreOutput;
import com.fullcycle.catalogo.admin.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.genre.Genre;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.CreateGenreRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() throws Exception {
        final var expectedName = "ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any())).thenReturn(CreateGenreOutput.from(expectedId));

        final var aRequest = post("/genres")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(aRequest)
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/genres/" + expectedId))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(createGenreUseCase).execute(
            argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.isActive())
            ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateGenre_thenShouldReturnNotificationException() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(createGenreUseCase.execute(any()))
            .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var aRequest = post("/genres")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(aRequest)
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createGenreUseCase).execute(
            argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.isActive())
            ));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreById_thenShouldReturnGenre() throws Exception {
        final var expectedName = "ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final var aGenre = Genre.newGenreWith(expectedName, expectedIsActive)
                .addCategories(expectedCategories.stream().map(CategoryID::from).toList());

        final var expectedId = aGenre.getId().getValue();

        when(getGenreByIdUseCase.execute(any())).thenReturn(GenreOutput.from(aGenre));

        final var aRequest = get("/genres/{id}", expectedId)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        mvc.perform(aRequest)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)))
            .andExpect(jsonPath("$.name", equalTo(expectedName)))
            .andExpect(jsonPath("$.categories_id", equalTo(expectedCategories)))
            .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
            .andExpect(jsonPath("$.created_at", equalTo(aGenre.getCreatedAt().toString())))
            .andExpect(jsonPath("$.updated_at", equalTo(aGenre.getUpdatedAt().toString())))
            .andExpect(jsonPath("$.deleted_at", equalTo(aGenre.getDeletedAt().toString())));

        verify(getGenreByIdUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetGenreById_thenShouldReturnNotFound() throws Exception {
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(getGenreByIdUseCase.execute(any())).thenThrow(NotFoundException.with(Genre.class, expectedId));

        final var aRequest = get("/genres/{id}", expectedId.getValue())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        mvc.perform(aRequest)
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(getGenreByIdUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_thenShouldReturnGenreId() throws Exception {
        final var expectedName = "ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var aGenre = Genre.newGenreWith(expectedName, expectedIsActive);

        final var expectedId = aGenre.getId().getValue();

        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any())).thenReturn(UpdateGenreOutput.from(expectedId));

        final var aRequest = put("/genres/{id}", expectedId)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(aRequest)
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateGenreUseCase).execute(
            argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.isActive())
            ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_thenShouldReturnNotificationException() throws Exception {
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aGenre = Genre.newGenreWith("Ação", expectedIsActive);

        final var expectedId = aGenre.getId().getValue();

        final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(updateGenreUseCase.execute(any()))
            .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        final var aRequest = put("/genres/{id}", expectedId)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(aRequest)
            .andDo(print())
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Location", nullValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.errors", hasSize(1)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateGenreUseCase).execute(
            argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                && Objects.equals(expectedCategories, cmd.categories())
                && Objects.equals(expectedIsActive, cmd.isActive())
            ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteGenre_thenShouldBeOk() throws Exception {
        final var expectedId = "123";

        doNothing().when(deleteGenreUseCase).execute(any());

        final var request =
            delete("/genres/{id}", expectedId).accept(APPLICATION_JSON);

        mvc.perform(request)
            .andExpect(status().isNoContent());

        verify(deleteGenreUseCase).execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListGenres_thenShouldReturnGenres() throws Exception {
        final var aGenre = Genre.newGenreWith("Ação", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(GenreListOutput.from(aGenre));

        when(listGenreUseCase.execute(any()))
            .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(APPLICATION_JSON);

        mvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
            .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(aGenre.getId().getValue())))
            .andExpect(jsonPath("$.items[0].name", equalTo(aGenre.getName())))
            .andExpect(jsonPath("$.items[0].is_active", equalTo(aGenre.isActive())))
            .andExpect(jsonPath("$.items[0].created_at", equalTo(aGenre.getCreatedAt().toString())))
            .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aGenre.getDeletedAt().toString())));

        verify(listGenreUseCase).execute(
            argThat(query ->
                Objects.equals(expectedPage, query.page())
                && Objects.equals(expectedPerPage, query.perPage())
                && Objects.equals(expectedDirection, query.direction())
                && Objects.equals(expectedSort, query.sort())
                && Objects.equals(expectedTerms, query.terms())
            ));
    }
}
