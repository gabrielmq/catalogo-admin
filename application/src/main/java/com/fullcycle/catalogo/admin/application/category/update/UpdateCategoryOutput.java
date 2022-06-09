package com.fullcycle.catalogo.admin.application.category.update;

import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;

public record UpdateCategoryOutput(String id) {
    public static UpdateCategoryOutput from(final Category aCategory) {
        return from(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutput from(final String anId) {
        return new UpdateCategoryOutput(anId);
    }
}
