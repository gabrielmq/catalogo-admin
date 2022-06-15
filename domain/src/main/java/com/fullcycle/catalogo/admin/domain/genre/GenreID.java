package com.fullcycle.catalogo.admin.domain.genre;

import com.fullcycle.catalogo.admin.domain.Identifier;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {
    private final String value;

    private GenreID(final String value) {
        this.value = Objects.requireNonNull(value, "'value' must not be null");
    }

    public static GenreID unique() {
        return from(UUID.randomUUID());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId);
    }

    public static GenreID from(final UUID anId) {
        return new GenreID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreID that = (GenreID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
