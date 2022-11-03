package com.fullcycle.catalogo.admin.infrastructure.utils;

import java.util.Objects;

public final class SQLUtils {

    private SQLUtils() {}

    public static String like(String term) {
        return Objects.nonNull(term) ? "%" + term + "%" : null;
    }
}
