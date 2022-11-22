package com.fullcycle.catalogo.admin.application.video.media.get;

import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.video.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.domain.video.VideoMediaType;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetMediaUseCase extends GetMediaUseCase {
    private final MediaResourceGateway gateway;

    public DefaultGetMediaUseCase(final MediaResourceGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public MediaOutput execute(final GetMediaCommand aCommand) {
        final var aType = VideoMediaType.of(aCommand.mediaType()).orElseThrow(typeNotFound(aCommand.mediaType()));
        final var anId = VideoID.from(aCommand.videoId());
        final var aResource = gateway.getResource(anId, aType)
                .orElseThrow(notFound(anId.getValue(), aType.name()));
        return MediaOutput.of(aResource.name(), aResource.contentType(), aResource.content());
    }

    private Supplier<NotFoundException> notFound(final String anId, final String aType) {
        return () -> NotFoundException.with(new Error("Resource %s was not found for video %s".formatted(aType, anId)));
    }

    private Supplier<NotFoundException> typeNotFound(final String aType) {
        return () -> NotFoundException.with(new Error("MediaType %s doesn't exists".formatted(aType)));
    }
}
