package com.fullcycle.catalogo.admin.infrastructure.category;

import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.category.CategorySearchQuery;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryMySQLGateway implements CategoryGateway {
    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(final CategoryID anId) {

    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return Optional.empty();
    }

    @Override
    public Category update(final Category aCategory) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        return null;
    }
}
