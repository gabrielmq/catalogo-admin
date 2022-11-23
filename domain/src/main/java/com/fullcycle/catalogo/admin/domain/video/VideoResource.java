package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.ValueObject;
import com.fullcycle.catalogo.admin.domain.resource.Resource;

import java.util.Objects;

public class VideoResource extends ValueObject {
    private final Resource resource;
    private final VideoMediaType type;

    private VideoResource(final Resource resource, final VideoMediaType type) {
        this.resource = Objects.requireNonNull(resource);
        this.type = Objects.requireNonNull(type);
    }

    public static VideoResource of(final Resource resource, final VideoMediaType type) {
        return new VideoResource(resource, type);
    }

    public Resource resource() {
        return resource;
    }

    public VideoMediaType type() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VideoResource that = (VideoResource) o;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
