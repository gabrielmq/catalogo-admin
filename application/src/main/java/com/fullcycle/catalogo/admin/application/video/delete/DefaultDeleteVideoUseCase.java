package com.fullcycle.catalogo.admin.application.video.delete;

import com.fullcycle.catalogo.admin.domain.video.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;

import java.util.Objects;

public class DefaultDeleteVideoUseCase extends DeleteVideoUseCase {
    private final VideoGateway videoGateway;
    private final MediaResourceGateway resourceGateway;

    public DefaultDeleteVideoUseCase(final VideoGateway videoGateway, final MediaResourceGateway resourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.resourceGateway = Objects.requireNonNull(resourceGateway);
    }

    @Override
    public void execute(final String anId) {
        final var aVideoId = VideoID.from(anId);
        videoGateway.deleteById(aVideoId);
        resourceGateway.clearResources(aVideoId);
    }
}
