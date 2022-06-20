package com.fullcycle.catalogo.admin.application.genre.retrieve.list;

import com.fullcycle.catalogo.admin.application.UseCase;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase
    extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}
