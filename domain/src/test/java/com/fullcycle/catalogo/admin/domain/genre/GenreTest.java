package com.fullcycle.catalogo.admin.domain.genre;

import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    @Test
    public void givenAnActiveGenre_whenCallDeactivate_thenShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenreWith(expectedName, true);

        assertNotNull(actualGenre);
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.deactivate();

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInactiveGenre_whenCallActivate_thenShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenreWith(expectedName, false);

        assertNotNull(actualGenre);
        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.activate();

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories().size());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveGenre_whenCallUpdateWithActive_thenShouldReturnGenreUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenreWith("acao", false);

        assertNotNull(actualGenre);
        assertFalse(actualGenre.isActive());
        assertNotNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidInactiveGenre_whenCallUpdateWithInactive_thenShouldReturnGenreUpdated() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryID.from("123"));

        final var actualGenre = Genre.newGenreWith("acao", true);

        assertNotNull(actualGenre);
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithEmptyName_thenShouldReceiveNotificationException() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualGenre = Genre.newGenreWith("acao", false);

        final var actualException =
            assertThrows(DomainException.class, () -> actualGenre.update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullName_thenShouldReceiveNotificationException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"));
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualGenre = Genre.newGenreWith("acao", false);

        final var actualException =
            assertThrows(DomainException.class, () -> actualGenre.update(expectedName, expectedIsActive, expectedCategories));

        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_thenShouldReceiveNotificationException() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = new ArrayList<>();

        final var actualGenre = Genre.newGenreWith("acao", expectedIsActive);

        assertNotNull(actualGenre);
        assertTrue(actualGenre.isActive());
        assertNull(actualGenre.getDeletedAt());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        assertDoesNotThrow(() -> actualGenre.update(expectedName, expectedIsActive, null));

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_thenShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesId = CategoryID.from("123");
        final var moviesId = CategoryID.from("456");
        final var expectedCategories = List.of(seriesId, moviesId);

        final var actualGenre = Genre.newGenreWith(expectedName, expectedIsActive);

        assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(seriesId);
        actualGenre.addCategory(moviesId);

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullAsCategoryId_whenCallAddCategory_thenShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final List<CategoryID> expectedCategories = new ArrayList<>();

        final var actualGenre = Genre.newGenreWith(expectedName, expectedIsActive);

        assertEquals(0, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.addCategory(null);

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_thenShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesId = CategoryID.from("123");
        final var moviesId = CategoryID.from("456");
        final var expectedCategories = List.of(moviesId);

        final var actualGenre = Genre.newGenreWith(expectedName, expectedIsActive)
                .update(expectedName, expectedIsActive, List.of(seriesId, moviesId));

        assertEquals(2, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(seriesId);

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidNullAsCategoryId_whenCallRemoveCategory_thenShouldReceiveOK() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var seriesId = CategoryID.from("123");
        final var moviesId = CategoryID.from("456");
        final var expectedCategories = List.of(moviesId);

        final var actualGenre = Genre.newGenreWith(expectedName, expectedIsActive)
                .update(expectedName, expectedIsActive, expectedCategories);

        assertEquals(1, actualGenre.getCategories().size());

        final var actualCreatedAt = actualGenre.getCreatedAt();
        final var actualUpdatedAt = actualGenre.getUpdatedAt();

        actualGenre.removeCategory(null);

        assertNotNull(actualGenre.getId());
        assertEquals(expectedName, actualGenre.getName());
        assertEquals(expectedIsActive, actualGenre.isActive());
        assertEquals(expectedCategories, actualGenre.getCategories());
        assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        assertNull(actualGenre.getDeletedAt());
    }
}
