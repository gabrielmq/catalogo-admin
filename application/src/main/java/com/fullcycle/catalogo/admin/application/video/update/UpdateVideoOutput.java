package com.fullcycle.catalogo.admin.application.video.update;

import com.fullcycle.catalogo.admin.domain.video.Video;

public record UpdateVideoOutput(String id) {
    public static UpdateVideoOutput from(final Video aVideo) {
        return from(aVideo.getId().getValue());
    }

    public static UpdateVideoOutput from(final String anId) {
        return new UpdateVideoOutput(anId);
    }
}
