package com.fullcycle.catalogo.admin.application.video.media.update;

import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.video.Video;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.domain.video.VideoMediaType;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.MediaStatus;

import java.util.Objects;
import java.util.function.Supplier;

import static com.fullcycle.catalogo.admin.domain.video.VideoMediaType.TRAILER;
import static com.fullcycle.catalogo.admin.domain.video.VideoMediaType.VIDEO;

public class DefaultUpdateMediaStatusUseCase extends UpdateMediaStatusUseCase {
    private final VideoGateway videoGateway;

    public DefaultUpdateMediaStatusUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final UpdateMediaStatusCommand aCommand) {
        final var anId = VideoID.from(aCommand.videoId());

        final var aVideo = videoGateway.findById(anId).orElseThrow(notFound(anId));

        final var encodedPath = "%s/%s".formatted(aCommand.folder(), aCommand.filename());
        if (matches(aCommand.resourceId(), aVideo.getVideo().orElse(null))) {
            update(VIDEO, aCommand.status(), aVideo, encodedPath);
        } else if (matches(aCommand.resourceId(), aVideo.getTrailer().orElse(null))) {
            update(TRAILER, aCommand.status(), aVideo, encodedPath);
        }
    }

    private void update(
        final VideoMediaType aType,
        final MediaStatus aStatus,
        final Video aVideo,
        final String encodedPath
    ) {
        switch (aStatus) {
            case PENDING -> {}
            case PROCESSING -> aVideo.processing(aType);
            case COMPLETED -> aVideo.completed(aType, encodedPath);
        }
        videoGateway.update(aVideo);
    }

    private boolean matches(final String anId, final AudioVideoMedia aMedia) {
        return Objects.nonNull(aMedia) && aMedia.id().equals(anId);
    }

    private Supplier<NotFoundException> notFound(final VideoID anId) {
        return () -> NotFoundException.with(Video.class, anId);
    }
}
