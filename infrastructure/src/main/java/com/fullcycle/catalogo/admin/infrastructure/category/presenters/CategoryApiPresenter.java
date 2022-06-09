package com.fullcycle.catalogo.admin.infrastructure.category.presenters;

import com.fullcycle.catalogo.admin.application.category.retrieve.get.CategoryOutput;
import com.fullcycle.catalogo.admin.infrastructure.category.models.CategoryAPIOutput;

public interface CategoryApiPresenter {
    static CategoryAPIOutput present(final CategoryOutput output) {
        return new CategoryAPIOutput(
            output.id().getValue(),
            output.name(),
            output.description(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt(),
            output.deletedAt()
        );
    }
}
