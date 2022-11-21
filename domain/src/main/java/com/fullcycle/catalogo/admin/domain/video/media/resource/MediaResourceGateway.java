package com.fullcycle.catalogo.admin.domain.video.media.resource;

import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;

public interface MediaResourceGateway {
    AudioVideoMedia storeAudioVideo(VideoID anId, Resource aResource);
    ImageMedia storeImage(VideoID anId, Resource aResource);
    void clearResources(VideoID anId);
}
