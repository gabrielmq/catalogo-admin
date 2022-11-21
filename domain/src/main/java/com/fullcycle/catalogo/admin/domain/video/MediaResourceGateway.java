package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;

public interface MediaResourceGateway {
    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);
    ImageMedia storeImage(VideoID anId, VideoResource aResource);
    void clearResources(VideoID anId);
}
