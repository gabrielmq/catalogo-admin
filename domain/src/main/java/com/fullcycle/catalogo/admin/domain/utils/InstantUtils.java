package com.fullcycle.catalogo.admin.domain.utils;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MICROS;

public final class InstantUtils {

    private InstantUtils() {}

    public static Instant now() {
        return Instant.now().truncatedTo(MICROS);
    }
}
