package com.fullcycle.catalogo.admin.infrastructure.configuration.usecases;

import com.fullcycle.catalogo.admin.application.video.media.update.DefaultUpdateMediaStatusUseCase;
import com.fullcycle.catalogo.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfiguration {
    private final VideoGateway videoGateway;

    public VideoUseCaseConfiguration(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Bean
    public UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new DefaultUpdateMediaStatusUseCase(videoGateway);
    }
}
