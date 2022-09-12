package com.fullcycle.catalogo.admin.domain.video.query;

public record VideoSearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction
) {
}
