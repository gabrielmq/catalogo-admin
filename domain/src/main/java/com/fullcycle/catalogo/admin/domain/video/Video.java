package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.AggregateRoot;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.event.DomainEvent;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.utils.InstantUtils;
import com.fullcycle.catalogo.admin.domain.validation.ValidationHandler;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;
import com.fullcycle.catalogo.admin.domain.video.rating.Rating;

import java.time.Instant;
import java.time.Year;
import java.util.*;

import static com.fullcycle.catalogo.admin.domain.video.VideoMediaType.*;
import static com.fullcycle.catalogo.admin.domain.video.VideoMediaType.TRAILER;

public class Video extends AggregateRoot<VideoID> {
    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    private Video(
        final VideoID anId,
        final String aTitle,
        final String aDescription,
        final Year aLaunchedAt,
        final double aDuration,
        final Rating aRating,
        final boolean wasOpened,
        final boolean wasPublished,
        final Instant aCreationDate,
        final Instant aUpdateDate,
        final ImageMedia aBanner,
        final ImageMedia aThumb,
        final ImageMedia aThumbHalf,
        final AudioVideoMedia aTrailer,
        final AudioVideoMedia aVideo,
        final Set<CategoryID> categories,
        final Set<GenreID> genres,
        final Set<CastMemberID> members,
        final List<DomainEvent> events
    ) {
        super(anId, events);
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedAt;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.banner = aBanner;
        this.thumbnail = aThumb;
        this.thumbnailHalf = aThumbHalf;
        this.trailer = aTrailer;
        this.video = aVideo;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = members;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    public static Video newVideo(
        final String aTitle,
        final String aDescription,
        final Year aLaunchedAt,
        final double aDuration,
        final Rating aRating,
        final boolean wasOpened,
        final boolean wasPublished,
        final Set<CategoryID> categories,
        final Set<GenreID> genres,
        final Set<CastMemberID> members
    ) {
        final var anId = VideoID.unique();
        final var now = InstantUtils.now();
        return new Video(
            anId,
            aTitle,
            aDescription,
            aLaunchedAt,
            aDuration,
            aRating,
            wasOpened,
            wasPublished,
            now,
            now,
            null,
            null,
            null,
            null,
            null,
            categories,
            genres,
            members,
            null
        );
    }

    public static Video with(final Video aVideo) {
        return new Video(
            aVideo.id,
            aVideo.title,
            aVideo.description,
            aVideo.launchedAt,
            aVideo.duration,
            aVideo.rating,
            aVideo.opened,
            aVideo.published,
            aVideo.createdAt,
            aVideo.updatedAt,
            aVideo.getBanner().orElse(null),
            aVideo.getThumbnail().orElse(null),
            aVideo.getThumbnailHalf().orElse(null),
            aVideo.getTrailer().orElse(null),
            aVideo.getVideo().orElse(null),
            new HashSet<>(aVideo.categories),
            new HashSet<>(aVideo.genres),
            new HashSet<>(aVideo.castMembers),
            aVideo.getEvents()
        );
    }

    public static Video with(
        final VideoID anId,
        final String aTitle,
        final String aDescription,
        final Year aLaunchedAt,
        final double aDuration,
        final Rating aRating,
        final boolean wasOpened,
        final boolean wasPublished,
        final Instant aCreationDate,
        final Instant aUpdateDate,
        final ImageMedia aBanner,
        final ImageMedia aThumb,
        final ImageMedia aThumbHalf,
        final AudioVideoMedia aTrailer,
        final AudioVideoMedia aVideo,
        final Set<CategoryID> categories,
        final Set<GenreID> genres,
        final Set<CastMemberID> members
    ) {
        return new Video(
            anId,
            aTitle,
            aDescription,
            aLaunchedAt,
            aDuration,
            aRating,
            wasOpened,
            wasPublished,
            aCreationDate,
            aUpdateDate,
            aBanner,
            aThumb,
            aThumbHalf,
            aTrailer,
            aVideo,
            new HashSet<>(categories),
            new HashSet<>(genres),
            new HashSet<>(members),
            null
        );
    }

    public Video update(
        final String aTitle,
        final String aDescription,
        final Year aLaunchedAt,
        final double aDuration,
        final Rating aRating,
        final boolean wasOpened,
        final boolean wasPublished,
        final Set<CategoryID> categories,
        final Set<GenreID> genres,
        final Set<CastMemberID> members
    ) {
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedAt;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setMembers(members);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video processing(final VideoMediaType aType) {
        if (VIDEO == aType) {
            getVideo().ifPresent(video -> setVideo(video.processing()));
        } else if (TRAILER == aType) {
            getTrailer().ifPresent(trailer -> setTrailer(trailer.processing()));
        }
        return this;
    }

    public Video completed(final VideoMediaType aType, final String encodedPath) {
        if (VIDEO == aType) {
            getVideo().ifPresent(video -> setVideo(video.completed(encodedPath)));
        } else if (TRAILER == aType) {
            getTrailer().ifPresent(trailer -> setTrailer(trailer.completed(encodedPath)));
        }
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Video setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Video setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Video setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Video setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Video setVideo(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Set<CategoryID> getCategories() {
        return Objects.nonNull(categories) ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    private void setCategories(final Set<CategoryID> categories) {
        this.categories = Objects.nonNull(categories) ? new HashSet<>(categories) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return Objects.nonNull(genres) ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    private void setGenres(final Set<GenreID> genres) {
        this.genres = Objects.nonNull(genres) ? new HashSet<>(genres) : Collections.emptySet();
    }

    public Set<CastMemberID> getMembers() {
        return Objects.nonNull(castMembers) ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }

    private void setMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = Objects.nonNull(castMembers) ? new HashSet<>(castMembers) : Collections.emptySet();
    }
}
