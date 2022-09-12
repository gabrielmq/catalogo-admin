package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.Identifier;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {
    private final String value;

    private VideoID(final String anId) {
        this.value = Objects.requireNonNull(anId);
    }

    public static VideoID unique() {
        return from(UUID.randomUUID());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
    }

    public static VideoID from(final UUID anId) {
        return from(anId.toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoID that = (VideoID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
