package com.fullcycle.catalogo.admin.domain.genre;

import com.fullcycle.catalogo.admin.domain.exceptions.DomainException;
import com.fullcycle.catalogo.admin.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GenreTest {

    @Test
    public void givenAValidParams_whenCallNewGenre_thenShouldInstantiateAGenre() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenreWith(expectedName, expectedIsActive);

        assertNotNull(actualGenre);
        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertNotNull(actualGenre.getCreatedAt());
        assertNotNull(actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewGenreAndValidate_thenShouldReceiveAnException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualException =
            assertThrows(DomainException.class, () -> Genre.newGenreWith(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewGenreAndValidate_thenShouldReceiveAnException() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualException =
                assertThrows(DomainException.class, () -> Genre.newGenreWith(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }

    @Test
    public void givenAnInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_thenShouldReceiveAnException() {
        final String expectedName = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualException =
                assertThrows(DomainException.class, () -> Genre.newGenreWith(expectedName, expectedIsActive));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    }
}
