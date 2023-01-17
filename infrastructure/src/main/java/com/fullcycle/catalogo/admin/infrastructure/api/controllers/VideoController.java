package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.application.video.create.CreateVideoCommand;
import com.fullcycle.catalogo.admin.application.video.create.CreateVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.media.get.GetMediaCommand;
import com.fullcycle.catalogo.admin.application.video.media.get.GetMediaUseCase;
import com.fullcycle.catalogo.admin.application.video.media.upload.UploadMediaCommand;
import com.fullcycle.catalogo.admin.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.list.ListVideosUseCase;
import com.fullcycle.catalogo.admin.application.video.update.UpdateVideoCommand;
import com.fullcycle.catalogo.admin.application.video.update.UpdateVideoUseCase;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.resource.Resource;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.video.VideoMediaType;
import com.fullcycle.catalogo.admin.domain.video.VideoResource;
import com.fullcycle.catalogo.admin.domain.video.query.VideoSearchQuery;
import com.fullcycle.catalogo.admin.infrastructure.api.VideoAPI;
import com.fullcycle.catalogo.admin.infrastructure.utils.HashingUtils;
import com.fullcycle.catalogo.admin.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.catalogo.admin.infrastructure.video.models.UpdateVideoRequest;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoListResponse;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoResponse;
import com.fullcycle.catalogo.admin.infrastructure.video.presenters.VideoAPIPresenter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import static com.fullcycle.catalogo.admin.domain.utils.CollectionUtils.mapTo;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
public class VideoController implements VideoAPI {
    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;
    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final ListVideosUseCase listVideosUseCase;
    private final GetMediaUseCase getMediaUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(
        final CreateVideoUseCase createVideoUseCase,
        final GetVideoByIdUseCase getVideoByIdUseCase,
        final UpdateVideoUseCase updateVideoUseCase,
        final DeleteVideoUseCase deleteVideoUseCase,
        final ListVideosUseCase listVideosUseCase,
        final GetMediaUseCase getMediaUseCase,
        final UploadMediaUseCase uploadMediaUseCase
    ) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public ResponseEntity<?> createFull(
        final String title,
        final String description,
        final Integer yearLaunched,
        final Double duration,
        final Boolean opened,
        final Boolean published,
        final String rating,
        final Set<String> categories,
        final Set<String> genres,
        final Set<String> members,
        final MultipartFile videoFile,
        final MultipartFile trailerFile,
        final MultipartFile bannerFile,
        final MultipartFile thumbFile,
        final MultipartFile thumbHalfFile
    ) {
        final var aCommand = CreateVideoCommand.with(
            title,
            description,
            yearLaunched,
            duration,
            opened,
            published,
            rating,
            categories,
            genres,
            members,
            resourceOf(videoFile),
            resourceOf(trailerFile),
            resourceOf(bannerFile),
            resourceOf(thumbFile),
            resourceOf(thumbHalfFile)
        );

        final var output = createVideoUseCase.execute(aCommand);
        return ResponseEntity
                .created(URI.create("/videos/%s".formatted(output.id())))
                .body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest aRequest) {
        final var aCommand = CreateVideoCommand.with(
            aRequest.title(),
            aRequest.description(),
            aRequest.yearLaunched(),
            aRequest.duration(),
            aRequest.opened(),
            aRequest.published(),
            aRequest.rating(),
            aRequest.categories(),
            aRequest.genres(),
            aRequest.members()
        );

        final var output = createVideoUseCase.execute(aCommand);
        return ResponseEntity
                .created(URI.create("/videos/%s".formatted(output.id())))
                .body(output);
    }

    @Override
    public Pagination<VideoListResponse> list(
        final String search,
        final int page,
        final int perPage,
        final String sort,
        final String direction,
        final Set<String> castMembers,
        final Set<String> categories,
        final Set<String> genres
    ) {
        final var aQuery = new VideoSearchQuery(
            page,
            perPage,
            search,
            sort,
            direction,
            mapTo(castMembers, CastMemberID::from),
            mapTo(categories, CategoryID::from),
            mapTo(genres, GenreID::from)
        );

        return VideoAPIPresenter.present(listVideosUseCase.execute(aQuery));
    }

    @Override
    public VideoResponse getById(final String anId) {
        return VideoAPIPresenter.present(getVideoByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<?> updateById(final String anId, final UpdateVideoRequest aRequest) {
        final var aCommand = UpdateVideoCommand.with(
            anId,
            aRequest.title(),
            aRequest.description(),
            aRequest.yearLaunched(),
            aRequest.duration(),
            aRequest.opened(),
            aRequest.published(),
            aRequest.rating(),
            aRequest.categories(),
            aRequest.genres(),
            aRequest.members()
        );

        final var output = updateVideoUseCase.execute(aCommand);
        return ResponseEntity.ok()
                .location(URI.create("/videos/%s".formatted(output.id())))
                .body(VideoAPIPresenter.present(output));
    }

    @Override
    public void deleteById(final String anId) {
        deleteVideoUseCase.execute(anId);
    }

    @Override
    public ResponseEntity<byte[]> getMediaByType(final String anId, final String aType) {
        final var output = getMediaUseCase.execute(GetMediaCommand.with(anId, aType));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(output.contentType()))
                .contentLength(output.content().length)
                .header(CONTENT_DISPOSITION, "attachment; filename=%s".formatted(output.name()))
                .body(output.content());
    }

    @Override
    public ResponseEntity<?> uploadMediaByType(final String anId, final String aType, final MultipartFile aMedia) {
        final var type = VideoMediaType.of(aType)
                .orElseThrow(() -> NotificationException.with(new Error("Invalid %s for VideoMediaType".formatted(aType))));

        final var aCommand = UploadMediaCommand.with(anId, VideoResource.of(resourceOf(aMedia), type));
        final var output = uploadMediaUseCase.execute(aCommand);
        return ResponseEntity
                .created(URI.create("/videos/%s/medias/%s".formatted(output.videoId(), output.mediaType())))
                .body(VideoAPIPresenter.present(output));
    }

    private Resource resourceOf(final MultipartFile aFile) {
        if (Objects.isNull(aFile)) {
            return null;
        }

        try {
            return Resource.of(
                HashingUtils.checksum(aFile.getBytes()),
                aFile.getBytes(),
                aFile.getContentType(),
                aFile.getOriginalFilename()
            );
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
