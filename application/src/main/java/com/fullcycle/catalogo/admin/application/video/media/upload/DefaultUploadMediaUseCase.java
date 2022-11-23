package com.fullcycle.catalogo.admin.application.video.media.upload;

import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.video.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.Video;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultUploadMediaUseCase extends UploadMediaUseCase {
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DefaultUploadMediaUseCase(
        final VideoGateway videoGateway,
        final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaCommand aCommand) {
        final var anId= VideoID.from(aCommand.videoId());
        final var aResource = aCommand.videoResource();

        final var aVideo = videoGateway.findById(anId).orElseThrow(notFound(anId));

        switch (aResource.type()) {
            case VIDEO -> aVideo.setVideo(mediaResourceGateway.storeAudioVideo(anId, aResource));
            case TRAILER -> aVideo.setTrailer(mediaResourceGateway.storeAudioVideo(anId, aResource));
            case BANNER -> aVideo.setBanner(mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL -> aVideo.setThumbnail(mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL_HALF -> aVideo.setThumbnailHalf(mediaResourceGateway.storeImage(anId, aResource));
        }
        return UploadMediaOutput.with(videoGateway.update(aVideo), aResource.type());
    }

    private Supplier<NotFoundException> notFound(final VideoID anId) {
        return () -> NotFoundException.with(Video.class, anId);
    }
}
