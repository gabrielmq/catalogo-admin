package com.fullcycle.catalogo.admin.domain.category;

import com.fullcycle.catalogo.admin.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public final class CategoryID extends Identifier {
    private final String value;

    private CategoryID(final String value) {
        this.value = Objects.requireNonNull(value, "'value' must not be null");
    }

    public static CategoryID unique() {
        return from(UUID.randomUUID());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    public static CategoryID from(final UUID anId) {
        return new CategoryID(anId.toString().toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryID that = (CategoryID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
