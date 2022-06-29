package com.fullcycle.catalogo.admin.infrastructure.category;

import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.category.persistence.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static com.fullcycle.catalogo.admin.infrastructure.utils.SpecificationUtils.like;
import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class CategoryMySQLGateway implements CategoryGateway {
    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(final CategoryID anId) {
        final var anIdValue = anId.getValue();
        if (categoryRepository.existsById(anIdValue)) {
            categoryRepository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return categoryRepository.findById(anId.getValue()).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
            aQuery.page(),
            aQuery.perPage(),
            Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var specifications = Optional
                .ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = categoryRepository.findAll(where(specifications), page);

        return new Pagination<>(
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Specification<CategoryJpaEntity> assembleSpecification(final String terms) {
        final Specification<CategoryJpaEntity> nameLike = like("name", terms);
        final Specification<CategoryJpaEntity> descriptionLike = like("description", terms);
        return nameLike.or(descriptionLike);
    }

    @Override
    public List<CategoryID> existsByIds(Iterable<CategoryID> categoryIDs) {
        final var ids = StreamSupport.stream(categoryIDs.spliterator(), false)
                .map(CategoryID::getValue)
                .toList();

        return categoryRepository.existsByIds(ids).stream()
                .map(CategoryID::from)
                .toList();
    }

    private Category save(Category aCategory) {
        return categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
