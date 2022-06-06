package com.fullcycle.catalogo.admin.application.category.update;

import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;

public record UpdateCategoryOutput(CategoryID id) {
    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
