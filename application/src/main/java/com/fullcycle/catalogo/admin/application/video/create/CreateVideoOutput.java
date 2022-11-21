package com.fullcycle.catalogo.admin.application.video.create;

import com.fullcycle.catalogo.admin.domain.video.Video;

public record CreateVideoOutput(String id) {
    public static CreateVideoOutput from(final Video aVideo) {
        return from(aVideo.getId().getValue());
    }

    public static CreateVideoOutput from(final String anId) {
        return new CreateVideoOutput(anId);
    }
}
