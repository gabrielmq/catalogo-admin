package com.fullcycle.catalogo.admin.application.video.retrieve.get;

import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.utils.CollectionUtils;
import com.fullcycle.catalogo.admin.domain.video.Video;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;
import com.fullcycle.catalogo.admin.domain.video.rating.Rating;

import java.time.Instant;
import java.util.Set;

public record VideoOutput(
    String id,
    String title,
    String description,
    int launchedAt,
    double duration,
    boolean opened,
    boolean published,
    Rating rating,
    Set<String> categories,
    Set<String> genres,
    Set<String> members,
    AudioVideoMedia video,
    AudioVideoMedia trailer,
    ImageMedia banner,
    ImageMedia thumbnail,
    ImageMedia thumbnailHalf,
    Instant createdAt,
    Instant updatedAt
) {
    public static VideoOutput from(final Video aVideo) {
        return new VideoOutput(
            aVideo.getId().getValue(),
            aVideo.getTitle(),
            aVideo.getDescription(),
            aVideo.getLaunchedAt().getValue(),
            aVideo.getDuration(),
            aVideo.isOpened(),
            aVideo.isPublished(),
            aVideo.getRating(),
            CollectionUtils.mapTo(aVideo.getCategories(), CategoryID::getValue),
            CollectionUtils.mapTo(aVideo.getGenres(), GenreID::getValue),
            CollectionUtils.mapTo(aVideo.getMembers(), CastMemberID::getValue),
            aVideo.getVideo().orElse(null),
            aVideo.getTrailer().orElse(null),
            aVideo.getBanner().orElse(null),
            aVideo.getThumbnail().orElse(null),
            aVideo.getThumbnailHalf().orElse(null),
            aVideo.getCreatedAt(),
            aVideo.getUpdatedAt()
        );
    }
}
