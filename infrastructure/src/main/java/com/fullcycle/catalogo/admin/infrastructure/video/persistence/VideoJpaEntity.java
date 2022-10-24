package com.fullcycle.catalogo.admin.infrastructure.video.persistence;

import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.video.Video;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.domain.video.rating.Rating;
import com.fullcycle.catalogo.admin.infrastructure.video.persistence.castmember.VideoCastMemberJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.video.persistence.category.VideoCategoryJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.video.persistence.genre.VideoGenreJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.video.persistence.media.AudioVideoMediaJpaEntity;
import com.fullcycle.catalogo.admin.infrastructure.video.persistence.media.ImageMediaJpaEntity;

import javax.persistence.*;
import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Table(name = "videos")
@Entity(name = "Video")
public class VideoJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "year_launched", nullable = false)
    private int yearLaunched;

    @Column(name = "opened", nullable = false)
    private boolean opened;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Enumerated(STRING)
    @Column(name = "rating")
    private Rating rating;

    @Column(name = "duration", precision = 2)
    private double duration;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @JoinColumn(name = "video_id")
    @OneToOne(cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private AudioVideoMediaJpaEntity video;

    @JoinColumn(name = "trailer_id")
    @OneToOne(cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private AudioVideoMediaJpaEntity trailer;

    @JoinColumn(name = "banner_id")
    @OneToOne(cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private ImageMediaJpaEntity banner;

    @JoinColumn(name = "thumbnail_id")
    @OneToOne(cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private ImageMediaJpaEntity thumbnail;

    @JoinColumn(name = "thumbnail_half_id")
    @OneToOne(cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private ImageMediaJpaEntity thumbnailHalf;

    @OneToMany(mappedBy = "video", cascade = ALL, orphanRemoval = true)
    private Set<VideoCategoryJpaEntity> categories;

    @OneToMany(mappedBy = "video", cascade = ALL, orphanRemoval = true)
    private Set<VideoGenreJpaEntity> genres;

    @OneToMany(mappedBy = "video", cascade = ALL, orphanRemoval = true)
    private Set<VideoCastMemberJpaEntity> castMembers;

    @Deprecated
    VideoJpaEntity() {}

    private VideoJpaEntity(
        final String id,
        final String title,
        final String description,
        final int yearLaunched,
        final boolean opened,
        final boolean published,
        final Rating rating,
        final double duration,
        final Instant createdAt,
        final Instant updatedAt,
        final AudioVideoMediaJpaEntity video,
        final AudioVideoMediaJpaEntity trailer,
        final ImageMediaJpaEntity banner,
        final ImageMediaJpaEntity thumbnail,
        final ImageMediaJpaEntity thumbnailHalf
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.yearLaunched = yearLaunched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.categories = new HashSet<>();
        this.genres = new HashSet<>();
        this.castMembers = new HashSet<>();
    }

    public static VideoJpaEntity from(final Video aVideo) {
        final var video = aVideo.getVideo()
                .map(AudioVideoMediaJpaEntity::from)
                .orElse(null);

        final var trailer = aVideo.getTrailer()
                .map(AudioVideoMediaJpaEntity::from)
                .orElse(null);

        final var banner = aVideo.getBanner()
                .map(ImageMediaJpaEntity::from)
                .orElse(null);

        final var thumbnail = aVideo.getThumbnail()
                .map(ImageMediaJpaEntity::from)
                .orElse(null);

        final var thumbnailHalf = aVideo.getThumbnailHalf()
                .map(ImageMediaJpaEntity::from)
                .orElse(null);

        final var entity = new VideoJpaEntity(
            aVideo.getId().getValue(),
            aVideo.getTitle(),
            aVideo.getDescription(),
            aVideo.getLaunchedAt().getValue(),
            aVideo.isOpened(),
            aVideo.isPublished(),
            aVideo.getRating(),
            aVideo.getDuration(),
            aVideo.getCreatedAt(),
            aVideo.getUpdatedAt(),
            video,
            trailer,
            banner,
            thumbnail,
            thumbnailHalf
        );

        aVideo.getCategories().forEach(entity::addCategory);
        aVideo.getGenres().forEach(entity::addGenre);
        aVideo.getMembers().forEach(entity::addCastMember);
        return entity;
    }

    public void addCategory(final CategoryID categoryID) {
        this.categories.add(VideoCategoryJpaEntity.from(this, categoryID));
    }

    public void addGenre(final GenreID genreID) {
        this.genres.add(VideoGenreJpaEntity.from(this, genreID));
    }

    public void addCastMember(final CastMemberID castMemberID) {
        this.castMembers.add(VideoCastMemberJpaEntity.from(this, castMemberID));
    }

    public Video toAggregate() {
        final var video = Optional.ofNullable(getVideo())
                .map(AudioVideoMediaJpaEntity::toDomain)
                .orElse(null);

        final var trailer = Optional.ofNullable(getTrailer())
                .map(AudioVideoMediaJpaEntity::toDomain)
                .orElse(null);

        final var banner = Optional.ofNullable(getBanner())
                .map(ImageMediaJpaEntity::toDomain)
                .orElse(null);

        final var thumbnail = Optional.ofNullable(getThumbnail())
                .map(ImageMediaJpaEntity::toDomain)
                .orElse(null);

        final var thumbnailHalf = Optional.ofNullable(getThumbnailHalf())
                .map(ImageMediaJpaEntity::toDomain)
                .orElse(null);

        final var categories = getCategories().stream()
                .map(category -> CategoryID.from(category.getId().getCategoryId()))
                .collect(Collectors.toSet());

        final var genres = getGenres().stream()
                .map(genre -> GenreID.from(genre.getId().getGenreId()))
                .collect(Collectors.toSet());

        final var members = getCastMembers().stream()
                .map(member -> CastMemberID.from(member.getId().getCastMemberId()))
                .collect(Collectors.toSet());

        return Video.with(
            VideoID.from(id),
            title,
            description,
            Year.of(yearLaunched),
            duration,
            rating,
            opened,
            published,
            createdAt,
            updatedAt,
            banner,
            thumbnail,
            thumbnailHalf,
            trailer,
            video,
            categories,
            genres,
            members
        );
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getYearLaunched() {
        return yearLaunched;
    }

    public void setYearLaunched(int yearLaunched) {
        this.yearLaunched = yearLaunched;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AudioVideoMediaJpaEntity getVideo() {
        return video;
    }

    public void setVideo(AudioVideoMediaJpaEntity video) {
        this.video = video;
    }

    public AudioVideoMediaJpaEntity getTrailer() {
        return trailer;
    }

    public void setTrailer(AudioVideoMediaJpaEntity trailer) {
        this.trailer = trailer;
    }

    public ImageMediaJpaEntity getBanner() {
        return banner;
    }

    public void setBanner(ImageMediaJpaEntity banner) {
        this.banner = banner;
    }

    public ImageMediaJpaEntity getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageMediaJpaEntity thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageMediaJpaEntity getThumbnailHalf() {
        return thumbnailHalf;
    }

    public void setThumbnailHalf(ImageMediaJpaEntity thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
    }

    public Set<VideoCategoryJpaEntity> getCategories() {
        return categories;
    }

    public void setCategories(Set<VideoCategoryJpaEntity> categories) {
        this.categories = categories;
    }

    public Set<VideoGenreJpaEntity> getGenres() {
        return genres;
    }

    public void setGenres(Set<VideoGenreJpaEntity> genres) {
        this.genres = genres;
    }

    public Set<VideoCastMemberJpaEntity> getCastMembers() {
        return castMembers;
    }

    public void setCastMembers(Set<VideoCastMemberJpaEntity> castMembers) {
        this.castMembers = castMembers;
    }
}
