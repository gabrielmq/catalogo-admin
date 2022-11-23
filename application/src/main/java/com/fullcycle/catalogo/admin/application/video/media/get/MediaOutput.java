package com.fullcycle.catalogo.admin.application.video.media.get;

public record MediaOutput(
    String name,
    String contentType,
    byte[] content
) {
    public static MediaOutput of(final String name, final String contentType, final byte[] content) {
        return new MediaOutput(name, contentType, content);
    }
}
