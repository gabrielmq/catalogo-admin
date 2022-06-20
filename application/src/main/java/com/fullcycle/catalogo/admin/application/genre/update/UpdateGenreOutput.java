package com.fullcycle.catalogo.admin.application.genre.update;

import com.fullcycle.catalogo.admin.domain.genre.Genre;

public record UpdateGenreOutput(String id) {
    public static UpdateGenreOutput from(final Genre aGenre) {
        return from(aGenre.getId().getValue());
    }

    public static UpdateGenreOutput from(final String anId) {
        return new UpdateGenreOutput(anId);
    }
}
