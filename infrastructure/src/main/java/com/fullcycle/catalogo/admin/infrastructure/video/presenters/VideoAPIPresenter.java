package com.fullcycle.catalogo.admin.infrastructure.video.presenters;

import com.fullcycle.catalogo.admin.application.video.retrieve.get.VideoOutput;
import com.fullcycle.catalogo.admin.application.video.update.UpdateVideoOutput;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;
import com.fullcycle.catalogo.admin.infrastructure.video.models.AudioVideoMediaResponse;
import com.fullcycle.catalogo.admin.infrastructure.video.models.ImageMediaResponse;
import com.fullcycle.catalogo.admin.infrastructure.video.models.UpdateVideoResponse;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoResponse;

import java.util.Optional;

public interface VideoAPIPresenter {
    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
            output.id(),
            output.title(),
            output.description(),
            output.launchedAt(),
            output.duration(),
            output.opened(),
            output.published(),
            output.rating().getName(),
            output.createdAt(),
            output.updatedAt(),
            present(output.banner()),
            present(output.thumbnail()),
            present(output.thumbnailHalf()),
            present(output.video()),
            present(output.trailer()),
            output.categories(),
            output.genres(),
            output.members()
        );
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia media) {
        return Optional
                .ofNullable(media)
                .map(med ->
                    new AudioVideoMediaResponse(
                        med.id(),
                        med.checksum(),
                        med.name(),
                        med.rawLocation(),
                        med.encodedLocation(),
                        med.status().name()
                    )
                ).orElse(null);
    }

    static ImageMediaResponse present(final ImageMedia image) {
        return Optional
                .ofNullable(image)
                .map(im ->
                    new ImageMediaResponse(
                        image.id(),
                        image.checksum(),
                        image.name(),
                        image.location()
                    )
                ).orElse(null);
    }

    static UpdateVideoResponse present(final UpdateVideoOutput output) {
        return new UpdateVideoResponse(output.id());
    }
}
