package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.video.query.VideoSearchQuery;

import java.util.Optional;

public interface VideoGateway {
    Video create(Video aVideo);
    Video update(Video aVideo);
    void deleteById(VideoID anId);
    Optional<Video> findById(VideoID anId);
    Pagination<VideoPreview> findAll(VideoSearchQuery aQuery);
}
