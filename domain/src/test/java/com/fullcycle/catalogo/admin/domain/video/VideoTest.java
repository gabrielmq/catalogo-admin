package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.UnitTest;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.utils.IDUtils;
import com.fullcycle.catalogo.admin.domain.utils.InstantUtils;
import com.fullcycle.catalogo.admin.domain.validation.handler.ThrowsValidationHandler;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;
import com.fullcycle.catalogo.admin.domain.video.rating.Rating;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsNewVideo_thenShouldInstantiate() {
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
        """;

        // when
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

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertNotNull(actualVideo.getCreatedAt());
        assertNotNull(actualVideo.getUpdatedAt());
        assertEquals(actualVideo.getCreatedAt(), actualVideo.getUpdatedAt());
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(published, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertTrue(actualVideo.getDomainEvents().isEmpty());

        assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdate_thenShouldReturnUpdated() {
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
        final var expectedEvent = new VideoMediaCreated("ID", "file");
        final var expectedEventCount = 1;
        final var expectedDescription = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;

        final var aVideo = Video.newVideo(
            "Test title",
            "Test description",
            Year.of(1999),
            0.0,
            Rating.AGE_10,
            true,
            true,
            Set.of(),
            Set.of(),
            Set.of()
        );

        aVideo.registerEvent(expectedEvent);

        // when
        final var actualVideo = Video.with(aVideo)
            .update(
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

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(published, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getMembers());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(expectedEventCount, actualVideo.getDomainEvents().size());
        assertEquals(expectedEvent, actualVideo.getDomainEvents().get(0));

        assertDoesNotThrow(() -> aVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateVideoMedia_thenShouldReturnUpdated() {
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
        """;

        final var aVideo = Video.newVideo(
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

        final var aVideoMedia = AudioVideoMedia.with(
            "abc",
            "Video.mp4",
            "/123/videos"
        );

        final var expectedDomainEvents = 1;

        // when
        final var actualVideo = Video.with(aVideo).updatedVideoMedia(aVideoMedia);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(published, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getMembers());
        assertEquals(aVideoMedia, actualVideo.getVideo().get());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(expectedDomainEvents, actualVideo.getDomainEvents().size());

        final var actualEvent = (VideoMediaCreated) actualVideo.getDomainEvents().get(0);
        assertEquals(aVideo.getId().getValue(), actualEvent.resourceId());
        assertEquals(aVideoMedia.rawLocation(), actualEvent.filePath());
        assertNotNull(actualEvent.occurredOn());

        assertDoesNotThrow(() -> aVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateTrailerMedia_thenShouldReturnUpdated() {
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
        """;

        final var aVideo = Video.newVideo(
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

        final var aTrailerMedia = AudioVideoMedia.with(
            "abc",
            "Trailer.mp4",
            "/123/videos"
        );

        final var expectedDomainEvents = 1;

        // when
        final var actualVideo = Video.with(aVideo).updateTrailerMedia(aTrailerMedia);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(published, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getMembers());
        assertEquals(aTrailerMedia, actualVideo.getTrailer().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());
        assertEquals(expectedDomainEvents, actualVideo.getDomainEvents().size());

        final var actualEvent = (VideoMediaCreated) actualVideo.getDomainEvents().get(0);
        assertEquals(aVideo.getId().getValue(), actualEvent.resourceId());
        assertEquals(aTrailerMedia.rawLocation(), actualEvent.filePath());
        assertNotNull(actualEvent.occurredOn());

        assertDoesNotThrow(() -> aVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateBannerMedia_thenShouldReturnUpdated() {
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
        """;

        final var aVideo = Video.newVideo(
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

        final var aBanner = ImageMedia.with(
            "abc",
            "Banner",
            "/123/videos"
        );

        // when
        final var actualVideo = Video.with(aVideo).updateBannerMedia(aBanner);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(published, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getMembers());
        assertEquals(aBanner, actualVideo.getBanner().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> aVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateThumbnailMedia_thenShouldReturnUpdated() {
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
        """;

        final var aVideo = Video.newVideo(
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

        final var aThumbnail = ImageMedia.with(
            "abc",
            "Thumbnail",
            "/123/videos"
        );

        // when
        final var actualVideo = Video.with(aVideo).updateThumbnailMedia(aThumbnail);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(published, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getMembers());
        assertEquals(aThumbnail, actualVideo.getThumbnail().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        assertDoesNotThrow(() -> aVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsUpdateThumbnailHalfMedia_thenShouldReturnUpdated() {
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
        """;

        final var aVideo = Video.newVideo(
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

        final var aThumbnailHalf = ImageMedia.with(
            "abc",
            "Thumbnail half",
            "/123/videos"
        );

        // when
        final var actualVideo = Video.with(aVideo).updateThumbnailHalfMedia(aThumbnailHalf);

        // then
        assertNotNull(actualVideo);
        assertNotNull(actualVideo.getId());
        assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        assertEquals(expectedTitle, actualVideo.getTitle());
        assertEquals(expectedDescription, actualVideo.getDescription());
        assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        assertEquals(expectedDuration, actualVideo.getDuration());
        assertEquals(expectedOpened, actualVideo.isOpened());
        assertEquals(published, actualVideo.isPublished());
        assertEquals(expectedRating, actualVideo.getRating());
        assertEquals(expectedCategories, actualVideo.getCategories());
        assertEquals(expectedGenres, actualVideo.getGenres());
        assertEquals(expectedMembers, actualVideo.getMembers());
        assertEquals(aThumbnailHalf, actualVideo.getThumbnailHalf().get());
        assertTrue(actualVideo.getVideo().isEmpty());
        assertTrue(actualVideo.getTrailer().isEmpty());
        assertTrue(actualVideo.getBanner().isEmpty());
        assertTrue(actualVideo.getThumbnail().isEmpty());

        assertDoesNotThrow(() -> aVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    public void givenValidVideo_whenCallsWith_thenShouldCreateWithoutEvents() {
        // given
        final var expectedId = VideoID.from(IDUtils.uuid());
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
        """;

        // when
        final var actualVideo = Video.with(
            expectedId,
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedRating,
            expectedOpened,
            published,
            InstantUtils.now(),
            InstantUtils.now(),
            null,
            null,
            null,
            null,
            null,
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        // then
        assertNotNull(actualVideo.getDomainEvents());
    }
}
