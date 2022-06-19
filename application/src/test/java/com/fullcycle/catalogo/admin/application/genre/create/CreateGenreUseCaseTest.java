package com.fullcycle.catalogo.admin.application.genre.create;

import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.genre.GenreGateway;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
            CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(genreGateway, times(1))
            .create(argThat(aGenre ->
                Objects.nonNull(aGenre.getId())
                && Objects.equals(expectedName, aGenre.getName())
                && Objects.equals(expectedIsActive, aGenre.isActive())
                && Objects.equals(expectedCategories, aGenre.getCategories())
                && Objects.nonNull(aGenre.getCreatedAt())
                && Objects.nonNull(aGenre.getUpdatedAt())
                && Objects.isNull(aGenre.getDeletedAt())));
    }

    @Test
    public void givenAValidCommandWithInactive_whenCallsCreateGenre_thenShouldReturnGenreId() {
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(genreGateway, times(1))
            .create(argThat(aGenre ->
                Objects.nonNull(aGenre.getId())
                && Objects.equals(expectedName, aGenre.getName())
                && Objects.equals(expectedIsActive, aGenre.isActive())
                && Objects.equals(expectedCategories, aGenre.getCategories())
                && Objects.nonNull(aGenre.getCreatedAt())
                && Objects.nonNull(aGenre.getUpdatedAt())
                && Objects.nonNull(aGenre.getDeletedAt())));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsCreateGenre_thenShouldReturnGenreId() {
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"), CategoryID.from("456"));

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any())).thenReturn(expectedCategories);
        when(genreGateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);

        verify(genreGateway, times(1))
            .create(argThat(aGenre ->
                Objects.nonNull(aGenre.getId())
                && Objects.equals(expectedName, aGenre.getName())
                && Objects.equals(expectedIsActive, aGenre.isActive())
                && Objects.equals(expectedCategories, aGenre.getCategories())
                && Objects.nonNull(aGenre.getCreatedAt())
                && Objects.nonNull(aGenre.getUpdatedAt())
                && Objects.isNull(aGenre.getDeletedAt())));
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsCreateGenre_thenShouldReturnNotificationException() {
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidNullName_whenCallsCreateGenre_thenShouldReturnNotificationException() {
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnNotificationException() {
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);
        final var expectedErrorMessage = "Some categories could not be found: 456, 789";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(filmes));

        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnNotificationException() {
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");

        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        final var aCommand =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any())).thenReturn(List.of(filmes));

        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }
}
