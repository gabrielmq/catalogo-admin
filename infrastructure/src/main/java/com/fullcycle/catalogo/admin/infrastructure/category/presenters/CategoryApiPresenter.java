package com.fullcycle.catalogo.admin.infrastructure.category.presenters;

import com.fullcycle.catalogo.admin.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.catalogo.admin.application.category.retrieve.list.CategoryListOutput;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryResponse;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {
    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
            output.id().getValue(),
            output.name(),
            output.description(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt(),
            output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
