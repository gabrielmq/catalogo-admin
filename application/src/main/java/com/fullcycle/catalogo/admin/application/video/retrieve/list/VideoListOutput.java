package com.fullcycle.catalogo.admin.application.video.retrieve.list;

import com.fullcycle.catalogo.admin.domain.video.VideoPreview;

import java.time.Instant;

public record VideoListOutput(
    String id,
    String title,
    String description,
    Instant createdAt,
    Instant updatedAt
) {
    public static VideoListOutput from(final VideoPreview aVideo) {
        return new VideoListOutput(
            aVideo.id(),
            aVideo.title(),
            aVideo.description(),
            aVideo.createdAt(),
            aVideo.updatedAt()
        );
    }
}
