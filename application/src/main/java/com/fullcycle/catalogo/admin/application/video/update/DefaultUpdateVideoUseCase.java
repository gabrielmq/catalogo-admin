package com.fullcycle.catalogo.admin.application.video.update;

import com.fullcycle.catalogo.admin.domain.Identifier;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.DomainException;
import com.fullcycle.catalogo.admin.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.genre.GenreGateway;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.validation.ValidationHandler;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import com.fullcycle.catalogo.admin.domain.video.*;
import com.fullcycle.catalogo.admin.domain.video.rating.Rating;

import java.time.Year;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.fullcycle.catalogo.admin.domain.video.VideoMediaType.*;
import static java.util.stream.Collectors.joining;

public class DefaultUpdateVideoUseCase extends UpdateVideoUseCase {
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final VideoGateway videoGateway;
    private final MediaResourceGateway resourceGateway;

    public DefaultUpdateVideoUseCase(
        final CategoryGateway categoryGateway,
        final GenreGateway genreGateway,
        final CastMemberGateway castMemberGateway,
        final VideoGateway videoGateway,
        final MediaResourceGateway resourceGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.resourceGateway = Objects.requireNonNull(resourceGateway);
    }

    @Override
    public UpdateVideoOutput execute(final UpdateVideoCommand aCommand) {
        final var anId = VideoID.from(aCommand.id());

        final var aRating = Optional.ofNullable(aCommand.rating()).flatMap(Rating::of).orElse(null);
        final var aLaunchYear = Optional.ofNullable(aCommand.launchedAt()).map(Year::of).orElse(null);
        final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
        final var genres = toIdentifier(aCommand.genres(), GenreID::from);
        final var members = toIdentifier(aCommand.members(), CastMemberID::from);

        final var aVideo = videoGateway.findById(anId).orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.append(validateGenres(genres));
        notification.append(validateMembers(members));

        aVideo.update(
            aCommand.title(),
            aCommand.description(),
            aLaunchYear,
            aCommand.duration(),
            aRating,
            aCommand.opened(),
            aCommand.published(),
            categories,
            genres,
            members
        );

        aVideo.validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException("Could not update Aggregate Video", notification);
        }

        return UpdateVideoOutput.from(update(aCommand, aVideo));
    }

    private Supplier<DomainException> notFound(final VideoID anId) {
        return () -> NotFoundException.with(Video.class, anId);
    }

    private Video update(final UpdateVideoCommand aCommand, final Video aVideo) {
        final var anId = aVideo.getId();

        try {
            aCommand.getVideo()
                .ifPresent(video -> {
                    final var videoResource = VideoResource.of(video, VIDEO);
                    aVideo.updatedVideoMedia(resourceGateway.storeAudioVideo(anId, videoResource));
                });

            aCommand.getTrailer()
                .ifPresent(trailer -> {
                    final var videoResource = VideoResource.of(trailer, TRAILER);
                    aVideo.updateTrailerMedia(resourceGateway.storeAudioVideo(anId, videoResource));
                });

            aCommand.getBanner()
                .ifPresent(banner -> {
                    final var videoResource = VideoResource.of(banner, BANNER);
                    aVideo.updateBannerMedia(resourceGateway.storeImage(anId, videoResource));
                });

            aCommand.getThumbnail()
                .ifPresent(thumb -> {
                    final var videoResource = VideoResource.of(thumb, THUMBNAIL);
                    aVideo.updateThumbnailMedia(resourceGateway.storeImage(anId, videoResource));
                });

            aCommand.getThumbnailHalf()
                .ifPresent(thumbHalf -> {
                    final var videoResource = VideoResource.of(thumbHalf, THUMBNAIL_HALF);
                    aVideo.updateThumbnailHalfMedia(resourceGateway.storeImage(anId, videoResource));
                });

            return videoGateway.update(aVideo);
        } catch (final Throwable throwable) {
            throw InternalErrorException.with(
                "An error on update video was observed [videoId:%s]".formatted(anId.getValue()),
                throwable
            );
        }
    }

    private ValidationHandler validateCategories(final Set<CategoryID> ids) {
        return validateAggregate("categories", ids, categoryGateway::existsByIds);
    }

    private ValidationHandler validateGenres(Set<GenreID> ids) {
        return validateAggregate("genres", ids, genreGateway::existsByIds);
    }

    private ValidationHandler validateMembers(Set<CastMemberID> ids) {
        return validateAggregate("members", ids, castMemberGateway::existsByIds);
    }

    private <T extends Identifier> ValidationHandler validateAggregate(
        final String aggregate,
        final Set<T> ids,
        final Function<Iterable<T>, List<T>> existsByIds
    ) {
        final var notification = Notification.create();
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = existsByIds.apply(ids);
        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(Identifier::getValue)
                    .collect(joining(", "));

            notification.append(new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
        }
        return notification;
    }

    private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
        return ids.stream().map(mapper).collect(Collectors.toSet());
    }
}
