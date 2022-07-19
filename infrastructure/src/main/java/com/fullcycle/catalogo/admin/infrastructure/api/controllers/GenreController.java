package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.application.genre.create.CreateGenreCommand;
import com.fullcycle.catalogo.admin.application.genre.create.CreateGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.catalogo.admin.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.update.UpdateGenreCommand;
import com.fullcycle.catalogo.admin.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;
import com.fullcycle.catalogo.admin.infrastructure.api.GenreAPI;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.GenreListResponse;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.GenreResponse;
import com.fullcycle.catalogo.admin.infrastructure.genre.models.UpdateGenreRequest;
import com.fullcycle.catalogo.admin.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final ListGenreUseCase listGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;

    public GenreController(
        final CreateGenreUseCase createGenreUseCase,
        final GetGenreByIdUseCase getGenreByIdUseCase,
        final ListGenreUseCase listGenreUseCase,
        final UpdateGenreUseCase updateGenreUseCase,
        final DeleteGenreUseCase deleteGenreUseCase
    ) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.listGenreUseCase = listGenreUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand =
            CreateGenreCommand.with(input.name(), input.isActive(), input.categories());

        final var output = createGenreUseCase.execute(aCommand);
        return ResponseEntity
                .created(URI.create("/genres/%s".formatted(output.id())))
                .body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(
        final String search,
        final int page,
        final int perPage,
        final String sort,
        final String direction
    ) {
        return listGenreUseCase
                .execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreApiPresenter::present);
    }

    @Override
    public ResponseEntity<GenreResponse> getById(final String id) {
        return ResponseEntity.ok(GenreApiPresenter.present(getGenreByIdUseCase.execute(id)));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        final var aCommand =
            UpdateGenreCommand.with(id, input.name(), input.isActive(), input.categories());
        return ResponseEntity.ok(updateGenreUseCase.execute(aCommand));
    }

    @Override
    public void deleteById(final String id) {
        deleteGenreUseCase.execute(id);
    }
}
