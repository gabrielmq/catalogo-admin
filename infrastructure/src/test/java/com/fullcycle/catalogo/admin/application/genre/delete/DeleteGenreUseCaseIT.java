package com.fullcycle.catalogo.admin.application.genre.delete;

import com.fullcycle.catalogo.admin.IntegrationTest;
import com.fullcycle.catalogo.admin.domain.genre.Genre;
import com.fullcycle.catalogo.admin.domain.genre.GenreGateway;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;


    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_thenShouldDeleteGenre() {
        final var aGenre = genreGateway.create(Genre.newGenreWith("Ação", true));

        final var expectedId = aGenre.getId();

        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_thenShouldBeOk() {
        genreGateway.create(Genre.newGenreWith("Ação", true));

        final var expectedId = GenreID.from("123");

        assertEquals(1, genreRepository.count());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(1, genreRepository.count());
    }
}
