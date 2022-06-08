package com.fullcycle.catalogo.admin.application.category.create;

import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;

public record CreateCategoryOutput(String id) {
    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from(final String anId) {
        return new CreateCategoryOutput(anId);
    }
}
