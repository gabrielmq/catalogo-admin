package com.fullcycle.catalogo.admin.infrastructure.configuration.usecases;

import com.fullcycle.catalogo.admin.application.video.create.CreateVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.create.DefaultCreateVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.delete.DefaultDeleteVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.media.get.DefaultGetMediaUseCase;
import com.fullcycle.catalogo.admin.application.video.media.get.GetMediaUseCase;
import com.fullcycle.catalogo.admin.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.fullcycle.catalogo.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.catalogo.admin.application.video.media.upload.DefaultUploadMediaUseCase;
import com.fullcycle.catalogo.admin.application.video.media.upload.UploadMediaUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.get.DefaultGetVideoByIdUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.list.DefaultListVideosUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.list.ListVideosUseCase;
import com.fullcycle.catalogo.admin.application.video.update.DefaultUpdateVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.update.UpdateVideoUseCase;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.genre.GenreGateway;
import com.fullcycle.catalogo.admin.domain.video.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfiguration {
    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public VideoUseCaseConfiguration(
        final VideoGateway videoGateway,
        final CategoryGateway categoryGateway,
        final CastMemberGateway castMemberGateway,
        final GenreGateway genreGateway,
        final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Bean
    public CreateVideoUseCase createVideoUseCase() {
        return new DefaultCreateVideoUseCase(categoryGateway, genreGateway, castMemberGateway, videoGateway, mediaResourceGateway);
    }

    @Bean
    public UpdateVideoUseCase updateVideoUseCase() {
        return new DefaultUpdateVideoUseCase(categoryGateway, genreGateway, castMemberGateway, videoGateway, mediaResourceGateway);
    }

    @Bean
    public GetVideoByIdUseCase getVideoByIdUseCase() {
        return new DefaultGetVideoByIdUseCase(videoGateway);
    }

    @Bean
    public DeleteVideoUseCase deleteVideoUseCase() {
        return new DefaultDeleteVideoUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public ListVideosUseCase listVideosUseCase() {
        return new DefaultListVideosUseCase(videoGateway);
    }

    @Bean
    public GetMediaUseCase getMediaUseCase() {
        return new DefaultGetMediaUseCase(mediaResourceGateway);
    }

    @Bean
    public UploadMediaUseCase uploadMediaUseCase() {
        return new DefaultUploadMediaUseCase(videoGateway, mediaResourceGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
