package com.fullcycle.catalogo.admin.domain.video.rating;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum Rating {
    ER("ER"),
    L("L"),
    AGE_10("10"),
    AGE_12("12"),
    AGE_14("14"),
    AGE_16("16"),
    AGE_18("18");

    private static final Map<String, Rating> RATINGS = new HashMap<>();

    static {
        for (Rating rating : values()) {
            RATINGS.put(rating.name, rating);
        }
    }

    private final String name;

    Rating(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Optional<Rating> of(final String label) {
        return Optional.ofNullable(RATINGS.get(label.toUpperCase()));
    }
}
