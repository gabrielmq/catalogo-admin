package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.Identifier;
import com.fullcycle.catalogo.admin.domain.utils.IDUtils;

import java.util.Objects;

public class VideoID extends Identifier {
    private final String value;

    private VideoID(final String anId) {
        this.value = Objects.requireNonNull(anId);
    }

    public static VideoID unique() {
        return from(IDUtils.uuid());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
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
