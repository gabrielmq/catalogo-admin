package com.fullcycle.catalogo.admin.application.category.retrieve.list;

import com.fullcycle.catalogo.admin.application.UseCase;
import com.fullcycle.catalogo.admin.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
