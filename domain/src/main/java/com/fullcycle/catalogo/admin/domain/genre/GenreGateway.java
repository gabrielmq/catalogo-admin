package com.fullcycle.catalogo.admin.domain.genre;

import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {
    Genre create(Genre aGenre);
    void deleteById(GenreID anId);
    Optional<Genre> findById(GenreID anId);
    Genre update(Genre aGenre);
    Pagination<Genre> findAll(SearchQuery aQuery);
}