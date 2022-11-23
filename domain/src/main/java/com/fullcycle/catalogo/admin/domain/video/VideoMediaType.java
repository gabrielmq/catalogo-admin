package com.fullcycle.catalogo.admin.domain.video;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public enum VideoMediaType {
    VIDEO,
    TRAILER,
    BANNER,
    THUMBNAIL,
    THUMBNAIL_HALF;

    private static final Map<String, VideoMediaType> MEDIA_TYPES = new HashMap<>();

    static {
        for (VideoMediaType value : values()) {
            MEDIA_TYPES.put(value.name(), value);
        }
    }

    public static Optional<VideoMediaType> of(final String value) {
        return Optional.ofNullable(MEDIA_TYPES.get(value.toUpperCase()));
    }
}
