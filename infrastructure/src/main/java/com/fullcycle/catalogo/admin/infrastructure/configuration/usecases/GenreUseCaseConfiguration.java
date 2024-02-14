package com.fullcycle.catalogo.admin.infrastructure.configuration.usecases;

import com.fullcycle.catalogo.admin.application.genre.create.CreateGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.create.DefaultCreateGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.delete.DefaultDeleteGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.fullcycle.catalogo.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.catalogo.admin.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.update.DefaultUpdateGenreUseCase;
import com.fullcycle.catalogo.admin.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(proxyBeanMethods = false)
public class GenreUseCaseConfiguration {
    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfiguration(final GenreGateway genreGateway, final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }
}
