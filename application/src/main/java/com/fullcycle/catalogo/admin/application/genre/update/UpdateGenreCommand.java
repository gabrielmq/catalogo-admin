package com.fullcycle.catalogo.admin.application.genre.update;

import java.util.List;
import java.util.Objects;

public record UpdateGenreCommand(
    String id,
    String name,
    boolean isActive,
    List<String> categories
) {
    public static UpdateGenreCommand with(
        final String anId,
        final String aName,
        final Boolean isActive,
        final List<String> categories
    ) {
        return new UpdateGenreCommand(anId, aName, Objects.requireNonNullElse(isActive, true), categories);
    }
}
