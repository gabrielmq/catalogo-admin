package com.fullcycle.catalogo.admin.domain.video.query;

import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;

import java.util.Set;

public record VideoSearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction,
    Set<CastMemberID> castMembers,
    Set<CategoryID> categories,
    Set<GenreID> genres
) {
}
