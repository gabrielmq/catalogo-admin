package com.fullcycle.catalogo.admin.infrastructure.genre;

import com.fullcycle.catalogo.admin.domain.genre.Genre;
import com.fullcycle.catalogo.admin.domain.genre.GenreGateway;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;
import com.fullcycle.catalogo.admin.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.genre.persistence.GenreRepository;
import com.fullcycle.catalogo.admin.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Component
public class GenreMySQLGateway implements GenreGateway {
    private final GenreRepository genreRepository;

    public GenreMySQLGateway(final GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre create(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public void deleteById(final GenreID anId) {
        final var anIdValue = anId.getValue();
        if (genreRepository.existsById(anIdValue)) {
            genreRepository.deleteById(anIdValue);
        }
    }

    @Override
    public Optional<Genre> findById(final GenreID anId) {
        return genreRepository.findById(anId.getValue()).map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Genre update(final Genre aGenre) {
        return save(aGenre);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
            aQuery.page(),
            aQuery.perPage(),
            Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var whereClause = Optional
            .ofNullable(aQuery.terms())
            .filter(str -> !str.isBlank())
            .map(this::assembleSpecification)
            .orElse(null);

        final var pageResult = genreRepository.findAll(where(whereClause), page);
        return new Pagination<>(
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    private Genre save(final Genre aGenre) {
        return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }
}
