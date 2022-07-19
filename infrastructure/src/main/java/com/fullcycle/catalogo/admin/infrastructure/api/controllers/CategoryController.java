package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryCommand;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryOutput;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.delete.DeleteCategoryUseCase;
import com.fullcycle.catalogo.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.fullcycle.catalogo.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.fullcycle.catalogo.admin.application.category.update.UpdateCategoryCommand;
import com.fullcycle.catalogo.admin.application.category.update.UpdateCategoryUseCase;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.infrastructure.api.CategoryAPI;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryListResponse;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryResponse;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CreateCategoryRequest;
import com.fullcycle.catalogo.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.fullcycle.catalogo.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.http.ResponseEntity.*;

@RestController
public class CategoryController implements CategoryAPI {
    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
        final CreateCategoryUseCase createCategoryUseCase,
        final GetCategoryByIdUseCase getCategoryByIdUseCase,
        final UpdateCategoryUseCase updateCategoryUseCase,
        final DeleteCategoryUseCase deleteCategoryUseCase,
        final ListCategoriesUseCase listCategoriesUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
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
    public Pagination<CategoryListResponse> listCategories(
        final String search,
        final int page,
        final int perPage,
        final String sort,
        final String direction
    ) {
        return listCategoriesUseCase
            .execute(new SearchQuery(page, perPage, search, sort, direction))
            .map(CategoryApiPresenter::present);
    }

    @Override
    public ResponseEntity<CategoryResponse> getById(final String id) {
        return ResponseEntity.ok(CategoryApiPresenter.present(getCategoryByIdUseCase.execute(id)));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest input) {
        final var aCommand =
            UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                Objects.nonNull(input.active()) ? input.active() : true
            );

        return updateCategoryUseCase.execute(aCommand).fold(unprocessableEntity()::body, ok()::body);
    }

    @Override
    public void deleteById(String id) {
        deleteCategoryUseCase.execute(id);
    }
}
