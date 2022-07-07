package com.fullcycle.catalogo.admin.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record UpdateGenreRequest(
    @JsonProperty("name") String name,
    @JsonProperty("categories_id") List<String> categories,
    @JsonProperty("is_active") Boolean active
) {
    public boolean isActive() {
        return Objects.requireNonNullElse(active, true);
    }

    public List<String> categories() {
        return Objects.requireNonNullElse(categories, Collections.emptyList());
    }
}
