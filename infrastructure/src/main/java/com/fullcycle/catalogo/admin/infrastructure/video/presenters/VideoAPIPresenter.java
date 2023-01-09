package com.fullcycle.catalogo.admin.infrastructure.video.presenters;

import com.fullcycle.catalogo.admin.application.video.retrieve.get.VideoOutput;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;
import com.fullcycle.catalogo.admin.infrastructure.video.models.AudioVideoMediaResponse;
import com.fullcycle.catalogo.admin.infrastructure.video.models.ImageMediaResponse;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoResponse;

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
        if (media == null) {
            return null;
        }
        return new AudioVideoMediaResponse(
            media.id(),
            media.checksum(),
            media.name(),
            media.rawLocation(),
            media.encodedLocation(),
            media.status().name()
        );
    }

    static ImageMediaResponse present(final ImageMedia image) {
        if (image == null) {
            return null;
        }
        return new ImageMediaResponse(
            image.id(),
            image.checksum(),
            image.name(),
            image.location()
        );
    }
}
