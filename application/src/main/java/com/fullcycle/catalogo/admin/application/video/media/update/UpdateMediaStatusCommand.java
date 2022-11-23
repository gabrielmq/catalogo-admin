package com.fullcycle.catalogo.admin.application.video.media.update;

import com.fullcycle.catalogo.admin.domain.video.media.MediaStatus;

public record UpdateMediaStatusCommand(
    MediaStatus status,
    String videoId,
    String resourceId,
    String folder,
    String filename
) {
    public static UpdateMediaStatusCommand with(
        final MediaStatus status,
        final String videoId,
        final String resourceId,
        final String folder,
        final String filename
    ) {
        return new UpdateMediaStatusCommand(status, videoId, resourceId, folder, filename);
    }
}
