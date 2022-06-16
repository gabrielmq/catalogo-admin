package com.fullcycle.catalogo.admin.domain.category;

import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category aCategory);
    void deleteById(CategoryID anId);
    Optional<Category> findById(CategoryID anId);
    Category update(Category aCategory);
    Pagination<Category> findAll(SearchQuery aQuery);
}
