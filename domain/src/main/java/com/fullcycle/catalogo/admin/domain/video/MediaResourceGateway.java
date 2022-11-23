package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.resource.Resource;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;

import java.util.Optional;

public interface MediaResourceGateway {
    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);
    ImageMedia storeImage(VideoID anId, VideoResource aResource);
    void clearResources(VideoID anId);
    Optional<Resource> getResource(VideoID anId, VideoMediaType aType);
}
