package com.fullcycle.catalogo.admin.application.genre.retrieve.list;

import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
    String name,
    boolean isActive,
    List<String> categories,
    Instant createdAt,
    Instant deletedAt
) {
    public static GenreListOutput from(final Genre aGenre) {
        return new GenreListOutput(
            aGenre.getName(),
            aGenre.isActive(),
            aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
            aGenre.getCreatedAt(),
            aGenre.getDeletedAt()
        );
    }
}
