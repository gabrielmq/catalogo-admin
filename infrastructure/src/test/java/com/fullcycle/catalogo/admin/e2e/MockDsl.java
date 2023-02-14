package com.fullcycle.catalogo.admin.e2e;

import com.fullcycle.catalogo.admin.domain.Identifier;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CastMemberResponse;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryResponse;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.catalogo.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.catalogo.admin.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.GenreResponse;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.function.Function;

import static com.fullcycle.catalogo.admin.APITest.ADMIN_JWT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {
    MockMvc mvc();

    /**
     * Category
     * */
    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var actualId =
            given("/categories", new CreateCategoryRequest(aName, aDescription, isActive));
        return CategoryID.from(actualId);
    }

    default ResultActions updateACategory(final CategoryID anId, final UpdateCategoryRequest aRequest) throws Exception {
        return update("/categories/", anId, aRequest);
    }

    default CategoryResponse retrieveACategory(final CategoryID anId) throws Exception {
        return retrieve("/categories/", anId,  CategoryResponse.class);
    }

    default ResultActions deleteACategory(final CategoryID anId) throws Exception {
        return delete("/categories/", anId);
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

    /**
     * Genre
     * */
    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var actualId =
            given("/genres", new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive));
        return GenreID.from(actualId);
    }

    default GenreResponse retrieveAGenre(final GenreID anId) throws Exception {
        return retrieve("/genres/", anId,  GenreResponse.class);
    }

    default ResultActions updateAGenre(final GenreID anId, final UpdateGenreRequest aRequest) throws Exception {
        return update("/genres/", anId, aRequest);
    }

    default ResultActions deleteAGenre(final GenreID anId) throws Exception {
        return delete("/genres/", anId);
    }

    default ResultActions listGenres(final int page, final int perPage) throws Exception {
        return listGenres(page, perPage, "", "", "");
    }

    default ResultActions listGenres(final int page, final int perPage, final String search) throws Exception {
        return listGenres(page, perPage, search, "", "");
    }

    default ResultActions listGenres(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return list("/genres", page, perPage, search, sort, direction);
    }

    /**
     * Cast Member
     * */
    default CastMemberID givenACastMember(final String aName, final CastMemberType aType) throws Exception {
        final var actualId = given("/cast_members", new CreateCastMemberRequest(aName, aType));
        return CastMemberID.from(actualId);
    }

    default ResultActions givenACastMemberResult(final String aName, final CastMemberType aType) throws Exception {
        return givenResult("/cast_members", new CreateCastMemberRequest(aName, aType));
    }

    default CastMemberResponse retrieveACastMember(final CastMemberID anId) throws Exception {
        return retrieve("/cast_members/", anId,  CastMemberResponse.class);
    }

    default ResultActions retrieveACastMemberResult(final CastMemberID anId) throws Exception {
        return retrieveResult("/cast_members/", anId);
    }

    default ResultActions updateACastMember(
        final CastMemberID anId,
        final String aName,
        final CastMemberType aType
    ) throws Exception {
        return update("/cast_members/", anId, new UpdateCastMemberRequest(aName, aType));
    }

    default ResultActions deleteACastMember(final CastMemberID anId) throws Exception {
        return delete("/cast_members/", anId);
    }

    default ResultActions listCastMembers(final int page, final int perPage) throws Exception {
        return listCastMembers(page, perPage, "", "", "");
    }

    default ResultActions listCastMembers(final int page, final int perPage, final String search) throws Exception {
        return listCastMembers(page, perPage, search, "", "");
    }

    default ResultActions listCastMembers(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return list("/cast_members", page, perPage, search, sort, direction);
    }

    default <T, R> List<T> mapTo(final List<R> src, final Function<R, T> mapper) {
        return src.stream().map(mapper).toList();
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .with(ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return mvc()
                .perform(aRequest)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted(url), "");
    }

    private ResultActions givenResult(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .with(ADMIN_JWT)
                .contentType(APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return mvc().perform(aRequest);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .with(ADMIN_JWT)
                .accept(APPLICATION_JSON_UTF8)
                .contentType(APPLICATION_JSON_UTF8);

        final var json = mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        return Json.readValue(json, clazz);
    }

    private ResultActions retrieveResult(final String url, final Identifier anId) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .with(ADMIN_JWT)
                .accept(APPLICATION_JSON_UTF8)
                .contentType(APPLICATION_JSON_UTF8);

        return mvc().perform(aRequest);
    }

    private ResultActions delete(final String url, final Identifier anId) throws Exception {
        final var aRequest =
            MockMvcRequestBuilders.delete(url + anId.getValue()).contentType(APPLICATION_JSON)
                    .with(ADMIN_JWT);
        return  mvc().perform(aRequest);
    }

    private ResultActions update(final String url, final Identifier anId, final Object body) throws Exception {
        final var aRequest = put(url + anId.getValue())
                .with(ADMIN_JWT)
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
                .with(ADMIN_JWT)
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
