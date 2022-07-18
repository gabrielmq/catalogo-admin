package com.fullcycle.catalogo.admin.e2e;

import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.catalogo.admin.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.CreateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {
    MockMvc mvc();

    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var actualId =
            given("/categories", new CreateCategoryRequest(aName, aDescription, isActive));
        return CategoryID.from(actualId);
    }

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var actualId =
            given("/genres", new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive));
        return GenreID.from(actualId);
    }

    private <T, R> List<T> mapTo(final List<R> src, final Function<R, T> mapper) {
        return src.stream().map(mapper).toList();
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return mvc()
                .perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");
    }
}
