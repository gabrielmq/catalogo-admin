package com.fullcycle.catalogo.admin.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryAPIInput(
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("is_active") Boolean active
) {
}
