package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryCommand;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryOutput;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.infrastructure.api.CategoryAPI;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryAPIOutput;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CreateCategoryAPIInput;
import com.fullcycle.catalogo.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.unprocessableEntity;

@RestController
public class CategoryController implements CategoryAPI {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryController(
        final CreateCategoryUseCase createCategoryUseCase,
        final GetCategoryByIdUseCase getCategoryByIdUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryAPIInput input) {
        final var aCommand =
            CreateCategoryCommand.with(
                input.name(),
                input.description(),
                Objects.nonNull(input.active()) ? input.active() : true
            );

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                created(URI.create("/categories/%s".formatted(output.id()))).body(output);

        return createCategoryUseCase.execute(aCommand)
                .fold(unprocessableEntity()::body, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(final String search,
                                        final int page,
                                        final int perPage,
                                        final String sort,
                                        final String dir) {
        return null;
    }

    @Override
    public ResponseEntity<CategoryAPIOutput> getById(final String id) {
        return ResponseEntity.ok(CategoryApiPresenter.present(getCategoryByIdUseCase.execute(id)));
    }
}
