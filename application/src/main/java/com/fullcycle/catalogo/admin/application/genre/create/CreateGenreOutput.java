package com.fullcycle.catalogo.admin.application.genre.create;

import com.fullcycle.catalogo.admin.domain.genre.Genre;

public record CreateGenreOutput(String id) {
    public static CreateGenreOutput from(final Genre aGenre) {
        return from(aGenre.getId().getValue());
    }

    public static CreateGenreOutput from(final String anId) {
        return new CreateGenreOutput(anId);
    }
}
