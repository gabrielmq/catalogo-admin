package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.DomainException;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.validation.handler.ThrowsValidationHandler;
import com.fullcycle.catalogo.admin.domain.video.rating.Rating;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VideoValidatorTest {

    @Test
    public void givenNullTitle_whenCallsValidate_thenShouldReceiveError() {
        // given
        final String expectedTitle = null;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var published = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDescription = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be null";

        final var actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            published,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        // when
        final var actualError =
            assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyTitle_whenCallsValidate_thenShouldReceiveError() {
        // given
        final var expectedTitle = "";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var published = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDescription = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' should not be empty";

        final var actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            published,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        // when
        final var actualError =
            assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenTitleWithLengthGraterThan255_whenCallsValidate_thenShouldReceiveError() {
        // given
        final var expectedTitle = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var published = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDescription = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'title' must be between 1 and 255 characters";

        final var actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            published,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        // when
        final var actualError =
            assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenEmptyDescription_whenCallsValidate_thenShouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var published = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDescription = "";

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' should not be empty";

        final var actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            published,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        // when
        final var actualError =
            assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenDescriptionWithLengthGraterThan4000_whenCallsValidate_thenShouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var published = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDescription = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
        """;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'description' must be between 1 and 4000 characters";

        final var actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            published,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        // when
        final var actualError =
            assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullLaunchedAt_whenCallsValidate_thenShouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interviews";
        final Year expectedLaunchedAt = null;
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var published = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDescription = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'launchedAt' should not be null";

        final var actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            published,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        // when
        final var actualError =
            assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }

    @Test
    public void givenNullRating_whenCallsValidate_thenShouldReceiveError() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.0;
        final var expectedOpened = false;
        final var published = false;
        final Rating expectedRating = null;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());
        final var expectedDescription = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'rating' should not be null";

        final var actualVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            published,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        // when
        final var actualError =
            assertThrows(DomainException.class, () -> actualVideo.validate(new ThrowsValidationHandler()));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());
    }
}
