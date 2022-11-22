package com.fullcycle.catalogo.admin.infrastructure.video;

import com.fullcycle.catalogo.admin.domain.resource.Resource;
import com.fullcycle.catalogo.admin.domain.video.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.domain.video.VideoMediaType;
import com.fullcycle.catalogo.admin.domain.video.VideoResource;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;
import com.fullcycle.catalogo.admin.infrastructure.configuration.properties.storage.StorageProperties;
import com.fullcycle.catalogo.admin.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {
    private final String filenamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService storageService) {
        this.filenamePattern = props.getFilenamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final var filepath = filepath(anId, videoResource);
        final var aResource = videoResource.resource();
        store(filepath, aResource);
        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource videoResource) {
        final var filepath = filepath(anId, videoResource);
        final var aResource = videoResource.resource();
        store(filepath, aResource);
        return ImageMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public void clearResources(final VideoID anId) {
        final var ids = storageService.list(folder(anId));
        storageService.deleteAll(ids);
    }

    private void store(final String filepath, final Resource aResource) {
        storageService.store(filepath, aResource);
    }

    private String filename(final VideoMediaType aType) {
        return filenamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filepath(final VideoID anId, final VideoResource aResource) {
        return folder(anId).concat("/").concat(filename(aResource.type()));
    }
}
