package com.fullcycle.catalogo.admin.application.video.retrieve.list;

import com.fullcycle.catalogo.admin.domain.video.Video;

import java.time.Instant;

public record VideoListOutput(
    String id,
    String title,
    String description,
    Instant createdAt,
    Instant updatedAt
) {
    public static VideoListOutput from(final Video aVideo) {
        return new VideoListOutput(
            aVideo.getId().getValue(),
            aVideo.getTitle(),
            aVideo.getDescription(),
            aVideo.getCreatedAt(),
            aVideo.getUpdatedAt()
        );
    }
}
