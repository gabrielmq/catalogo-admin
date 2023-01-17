package com.fullcycle.catalogo.admin.application.video.retrieve.list;

import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import com.fullcycle.catalogo.admin.domain.video.query.VideoSearchQuery;

import java.util.Objects;

public class DefaultListVideosUseCase extends ListVideosUseCase {
    private final VideoGateway videoGateway;

    public DefaultListVideosUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(final VideoSearchQuery aQuery) {
        return videoGateway.findAll(aQuery).map(VideoListOutput::from);
    }
}
