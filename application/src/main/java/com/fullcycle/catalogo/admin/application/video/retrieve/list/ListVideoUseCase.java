package com.fullcycle.catalogo.admin.application.video.retrieve.list;

import com.fullcycle.catalogo.admin.application.UseCase;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.video.query.VideoSearchQuery;

public abstract class ListVideoUseCase
    extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
