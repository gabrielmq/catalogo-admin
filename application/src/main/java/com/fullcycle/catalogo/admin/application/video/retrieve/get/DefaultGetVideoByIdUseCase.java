package com.fullcycle.catalogo.admin.application.video.retrieve.get;

import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.video.Video;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;

import java.util.Objects;

public class DefaultGetVideoByIdUseCase extends GetVideoByIdUseCase {
    private final VideoGateway videoGateway;

    public DefaultGetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anId) {
        final var videoId = VideoID.from(anId);
        return videoGateway.findById(videoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, videoId));
    }
}
