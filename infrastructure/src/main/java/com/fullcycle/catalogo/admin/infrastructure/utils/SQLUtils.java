package com.fullcycle.catalogo.admin.infrastructure.utils;

import java.util.Objects;

public final class SQLUtils {

    private SQLUtils() {}

    public static String like(final String term) {
        return Objects.nonNull(term) ? "%" + term + "%" : null;
    }

    public static String upper(final String term) {
        return Objects.nonNull(term) ? term.toUpperCase() : null;
    }
}
