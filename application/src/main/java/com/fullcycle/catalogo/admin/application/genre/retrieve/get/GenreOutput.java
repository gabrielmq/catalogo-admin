package com.fullcycle.catalogo.admin.application.genre.retrieve.get;

import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreOutput(
    String id,
    String name,
    boolean isActive,
    List<String> categories,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {
    public static GenreOutput from(final Genre aGenre) {
        return new GenreOutput(
            aGenre.getId().getValue(),
            aGenre.getName(),
            aGenre.isActive(),
            aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
            aGenre.getCreatedAt(),
            aGenre.getUpdatedAt(),
            aGenre.getDeletedAt()
        );
    }
}
