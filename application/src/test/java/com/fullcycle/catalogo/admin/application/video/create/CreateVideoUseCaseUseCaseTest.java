package com.fullcycle.catalogo.admin.application.video.create;

import com.fullcycle.catalogo.admin.domain.Fixture;
import com.fullcycle.catalogo.admin.application.UseCaseTest;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.genre.GenreGateway;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.utils.IDUtils;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;
import com.fullcycle.catalogo.admin.domain.video.media.MediaStatus;
import com.fullcycle.catalogo.admin.domain.video.media.resource.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.media.resource.Resource;
import com.fullcycle.catalogo.admin.domain.video.media.resource.Type;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class CreateVideoUseCaseUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private MediaResourceGateway resourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(
            videoGateway,
            categoryGateway,
            genreGateway,
            castMemberGateway,
            resourceGateway
        );
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideo_thenShouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.category().getId());
        final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.member().getId());
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualResult = useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway)
            .create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                && Objects.equals(expectedDescription, actualVideo.getDescription())
                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                && Objects.equals(expectedDuration, actualVideo.getDuration())
                && Objects.equals(expectedOpened, actualVideo.isOpened())
                && Objects.equals(expectedPublished, actualVideo.isPublished())
                && Objects.equals(expectedRating, actualVideo.getRating())
                && Objects.equals(expectedCategories, actualVideo.getCategories())
                && Objects.equals(expectedGenres, actualVideo.getGenres())
                && Objects.equals(expectedMembers, actualVideo.getMembers())
                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
            ));
    }

    @Test
    public void givenAValidCommandWithoutCategories_whenCallsCreateVideo_thenShouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.member().getId());
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualResult = useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway)
            .create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                && Objects.equals(expectedDescription, actualVideo.getDescription())
                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                && Objects.equals(expectedDuration, actualVideo.getDuration())
                && Objects.equals(expectedOpened, actualVideo.isOpened())
                && Objects.equals(expectedPublished, actualVideo.isPublished())
                && Objects.equals(expectedRating, actualVideo.getRating())
                && Objects.equals(expectedCategories, actualVideo.getCategories())
                && Objects.equals(expectedGenres, actualVideo.getGenres())
                && Objects.equals(expectedMembers, actualVideo.getMembers())
                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
            ));
    }

    @Test
    public void givenAValidCommandWithoutGenres_whenCallsCreateVideo_thenShouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.category().getId());
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.of(Fixture.CastMembers.member().getId());
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualResult = useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway)
            .create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                && Objects.equals(expectedDescription, actualVideo.getDescription())
                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                && Objects.equals(expectedDuration, actualVideo.getDuration())
                && Objects.equals(expectedOpened, actualVideo.isOpened())
                && Objects.equals(expectedPublished, actualVideo.isPublished())
                && Objects.equals(expectedRating, actualVideo.getRating())
                && Objects.equals(expectedCategories, actualVideo.getCategories())
                && Objects.equals(expectedGenres, actualVideo.getGenres())
                && Objects.equals(expectedMembers, actualVideo.getMembers())
                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
            ));
    }

    @Test
    public void givenAValidCommandWithoutMembers_whenCallsCreateVideo_thenShouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.category().getId());
        final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualResult = useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway)
            .create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                && Objects.equals(expectedDescription, actualVideo.getDescription())
                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                && Objects.equals(expectedDuration, actualVideo.getDuration())
                && Objects.equals(expectedOpened, actualVideo.isOpened())
                && Objects.equals(expectedPublished, actualVideo.isPublished())
                && Objects.equals(expectedRating, actualVideo.getRating())
                && Objects.equals(expectedCategories, actualVideo.getCategories())
                && Objects.equals(expectedGenres, actualVideo.getGenres())
                && Objects.equals(expectedMembers, actualVideo.getMembers())
                && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
            ));
    }

    @Test
    public void givenAValidCommandWithoutResources_whenCallsCreateVideo_thenShouldReturnVideoId() {
        // given
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.category().getId());
        final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.member().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

        // when
        final var actualResult = useCase.execute(aCommand);

        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.id());

        verify(videoGateway)
            .create(argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                && Objects.equals(expectedDescription, actualVideo.getDescription())
                && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                && Objects.equals(expectedDuration, actualVideo.getDuration())
                && Objects.equals(expectedOpened, actualVideo.isOpened())
                && Objects.equals(expectedPublished, actualVideo.isPublished())
                && Objects.equals(expectedRating, actualVideo.getRating())
                && Objects.equals(expectedCategories, actualVideo.getCategories())
                && Objects.equals(expectedGenres, actualVideo.getGenres())
                && Objects.equals(expectedMembers, actualVideo.getMembers())
                && actualVideo.getVideo().isEmpty()
                && actualVideo.getTrailer().isEmpty()
                && actualVideo.getBanner().isEmpty()
                && actualVideo.getThumbnail().isEmpty()
                && actualVideo.getThumbnailHalf().isEmpty()
            ));
    }

    @Test
    public void givenANullTitle_whenCallsCreateVideo_thenShouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'title' should not be null";
        final var expectedErrorCount = 1;

        final String expectedTitle = null;
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        // when
        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, times(0)).create(any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(resourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(resourceGateway, times(0)).storeImage(any(), any());
        verify(resourceGateway, times(0)).clearResources(any());
    }

    @Test
    public void givenAEmptyTitle_whenCallsCreateVideo_thenShouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        // when
        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, times(0)).create(any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(resourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(resourceGateway, times(0)).storeImage(any(), any());
        verify(resourceGateway, times(0)).clearResources(any());
    }

    @Test
    public void givenANullRating_whenCallsCreateVideo_thenShouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = null;
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        // when
        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, times(0)).create(any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(resourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(resourceGateway, times(0)).storeImage(any(), any());
        verify(resourceGateway, times(0)).clearResources(any());
    }

    @Test
    public void givenAnInvalidRating_whenCallsCreateVideo_thenShouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'rating' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final String expectedRating = "Invalid";
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        // when
        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, times(0)).create(any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(resourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(resourceGateway, times(0)).storeImage(any(), any());
        verify(resourceGateway, times(0)).clearResources(any());
    }

    @Test
    public void givenANullLaunchYear_whenCallsCreateVideo_thenShouldReturnDomainException() {
        // given
        final var expectedErrorMessage = "'launchedAt' should not be null";
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final Integer expectedLaunchYear = null;
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.<CategoryID>of();
        final var expectedGenres = Set.<GenreID>of();
        final var expectedMembers = Set.<CastMemberID>of();
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        // when
        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(videoGateway, times(0)).create(any());
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).existsByIds(any());
        verify(castMemberGateway, times(0)).existsByIds(any());
        verify(resourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(resourceGateway, times(0)).storeImage(any(), any());
        verify(resourceGateway, times(0)).clearResources(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeCategoriesDoesNotExists_thenShouldReturnDomainException() {
        // given
        final var aCategoryId = Fixture.Categories.category().getId();
        final var expectedErrorMessage = "Some categories could not be found: %s".formatted(aCategoryId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aCategoryId);
        final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.member().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>());
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

        // when
        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway).existsByIds(eq(expectedCategories));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(videoGateway, times(0)).create(any());
        verify(resourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(resourceGateway, times(0)).storeImage(any(), any());
        verify(resourceGateway, times(0)).clearResources(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeGenresDoesNotExists_thenShouldReturnDomainException() {
        // given
        final var aGenreId = Fixture.Genres.genre().getId();
        final var expectedErrorMessage = "Some genres could not be found: %s".formatted(aGenreId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.category().getId());
        final var expectedGenres = Set.of(aGenreId);
        final var expectedMembers = Set.of(Fixture.CastMembers.member().getId());
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>());

        // when
        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway).existsByIds(eq(expectedCategories));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(videoGateway, times(0)).create(any());
        verify(resourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(resourceGateway, times(0)).storeImage(any(), any());
        verify(resourceGateway, times(0)).clearResources(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoAndSomeMembersDoesNotExists_thenShouldReturnDomainException() {
        // given
        final var aMemberId = Fixture.CastMembers.member().getId();
        final var expectedErrorMessage = "Some members could not be found: %s".formatted(aMemberId.getValue());
        final var expectedErrorCount = 1;

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.category().getId());
        final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
        final var expectedMembers = Set.of(aMemberId);
        final Resource expectedVideo = null;
        final Resource expectedTrailer = null;
        final Resource expectedBanner = null;
        final Resource expectedThumb = null;
        final Resource expectedThumbHalf = null;

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>());
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

        // when
        final var actualException =
                assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());

        verify(categoryGateway).existsByIds(eq(expectedCategories));
        verify(genreGateway).existsByIds(eq(expectedGenres));
        verify(castMemberGateway).existsByIds(eq(expectedMembers));
        verify(videoGateway, times(0)).create(any());
        verify(resourceGateway, times(0)).storeAudioVideo(any(), any());
        verify(resourceGateway, times(0)).storeImage(any(), any());
        verify(resourceGateway, times(0)).clearResources(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateVideoThrowsException_thenShouldCallClearResources() {
        // given
        final var expectedErrorMessage = "An error on create video was observed [videoId:";

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(Fixture.Categories.category().getId());
        final var expectedGenres = Set.of(Fixture.Genres.genre().getId());
        final var expectedMembers = Set.of(Fixture.CastMembers.member().getId());
        final Resource expectedVideo = Fixture.Videos.resource(Type.VIDEO);
        final Resource expectedTrailer = Fixture.Videos.resource(Type.TRAILER);
        final Resource expectedBanner = Fixture.Videos.resource(Type.BANNER);
        final Resource expectedThumb = Fixture.Videos.resource(Type.THUMBNAIL);
        final Resource expectedThumbHalf = Fixture.Videos.resource(Type.THUMBNAIL_HALF);

        final var aCommand = CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf
        );

        when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));
        when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));
        when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));
        mockImageMedia();
        mockAudioVideoMedia();
        when(videoGateway.create(any())).thenThrow(new RuntimeException("Internal Server Error"));

        // when
        final var actualException =
            assertThrows(InternalErrorException.class, () -> useCase.execute(aCommand));

        // then
        assertNotNull(actualException);
        assertTrue(actualException.getMessage().startsWith(expectedErrorMessage));

        verify(resourceGateway).clearResources(any());
    }

    private void mockImageMedia() {
        when(resourceGateway.storeImage(any(), any()))
            .thenAnswer(answer -> {
                final var resource = answer.getArgument(1, Resource.class);
                return ImageMedia.with(IDUtils.uuid(), resource.name(), "/img");
            });
    }

    private void mockAudioVideoMedia() {
        when(resourceGateway.storeAudioVideo(any(), any()))
            .thenAnswer(answer -> {
                final var resource = answer.getArgument(1, Resource.class);
                return AudioVideoMedia.with(
                    IDUtils.uuid(),
                    resource.name(),
                    "/video",
                    "",
                    MediaStatus.PENDING
                );
            });
    }

}
