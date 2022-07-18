package com.fullcycle.catalogo.admin.e2e;

import com.fullcycle.catalogo.admin.domain.Identifier;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryResponse;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.catalogo.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.catalogo.admin.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.CreateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {
    MockMvc mvc();

    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var actualId =
            given("/categories", new CreateCategoryRequest(aName, aDescription, isActive));
        return CategoryID.from(actualId);
    }

    default ResultActions updateACategory(final Identifier anId, final UpdateCategoryRequest aRequest) throws Exception {
        return update("/categories/", anId, aRequest);
    }

    default CategoryResponse retrieveACategory(final Identifier anId) throws Exception {
        return retrieve("/categories/", anId,  CategoryResponse.class);
    }

    default ResultActions deleteACategory(final Identifier anId) throws Exception {
        return delete("/categories/", anId);
    }

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var actualId =
            given("/genres", new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive));
        return GenreID.from(actualId);
    }

    default ResultActions listCategories(final int page, final int perPage) throws Exception {
        return listCategories(page, perPage, "", "", "");
    }

    default ResultActions listCategories(final int page, final int perPage, final String search) throws Exception {
        return listCategories(page, perPage, search, "", "");
    }

    default ResultActions listCategories(
        final int page,
        final int perPage,
        final String search,
        final String sort,
        final String direction
    ) throws Exception {
        return list("/categories", page, perPage, search, sort, direction);
    }

    private <T, R> List<T> mapTo(final List<R> src, final Function<R, T> mapper) {
        return src.stream().map(mapper).toList();
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .contentType(APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return mvc()
                .perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        final var json = mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest =
            MockMvcRequestBuilders.delete(url + anId.getValue()).contentType(APPLICATION_JSON);
        return  mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object body) throws Exception {
        final var aRequest = put(url + anId.getValue())
                .contentType(APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return mvc().perform(aRequest);
    }

    private ResultActions list(
        final String url,
        final int page,
        final int perPage,
        final String search,
        final String sort,
        final String direction
    ) throws Exception {
        final var aRequest = get(url)
                .queryParam("page", String.valueOf(page))
                .queryParam("perPage", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("dir", direction)
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON);

        return mvc().perform(aRequest);
    }
}
