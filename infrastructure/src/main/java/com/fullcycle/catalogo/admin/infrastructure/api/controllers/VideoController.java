package com.fullcycle.catalogo.admin.infrastructure.api.controllers;

import com.fullcycle.catalogo.admin.application.video.create.CreateVideoCommand;
import com.fullcycle.catalogo.admin.application.video.create.CreateVideoUseCase;
import com.fullcycle.catalogo.admin.domain.resource.Resource;
import com.fullcycle.catalogo.admin.infrastructure.api.VideoAPI;
import com.fullcycle.catalogo.admin.infrastructure.utils.HashingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

@RestController
public class VideoController implements VideoAPI {
    private final CreateVideoUseCase createVideoUseCase;

    public VideoController(final CreateVideoUseCase createVideoUseCase) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
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
