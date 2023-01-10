package com.fullcycle.catalogo.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.catalogo.admin.ControllerTest;
import com.fullcycle.catalogo.admin.application.video.create.CreateVideoCommand;
import com.fullcycle.catalogo.admin.application.video.create.CreateVideoOutput;
import com.fullcycle.catalogo.admin.application.video.create.CreateVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.get.VideoOutput;
import com.fullcycle.catalogo.admin.application.video.retrieve.list.ListVideoUseCase;
import com.fullcycle.catalogo.admin.application.video.retrieve.list.VideoListOutput;
import com.fullcycle.catalogo.admin.application.video.update.UpdateVideoCommand;
import com.fullcycle.catalogo.admin.application.video.update.UpdateVideoOutput;
import com.fullcycle.catalogo.admin.application.video.update.UpdateVideoUseCase;
import com.fullcycle.catalogo.admin.domain.Fixture;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.genre.GenreID;
import com.fullcycle.catalogo.admin.domain.pagination.Pagination;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.video.Video;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.domain.video.VideoMediaType;
import com.fullcycle.catalogo.admin.domain.video.VideoPreview;
import com.fullcycle.catalogo.admin.domain.video.query.VideoSearchQuery;
import com.fullcycle.catalogo.admin.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.catalogo.admin.infrastructure.video.models.UpdateVideoRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.fullcycle.catalogo.admin.domain.utils.CollectionUtils.mapTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
public class VideoAPITest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

    @MockBean
    private GetVideoByIdUseCase getVideoByIdUseCase;

    @MockBean
    private UpdateVideoUseCase updateVideoUseCase;

    @MockBean
    private DeleteVideoUseCase deleteVideoUseCase;

    @MockBean
    private ListVideoUseCase listVideosUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateFull_thenShouldReturnAnId() throws Exception {
        // given
        final var categoryID = Fixture.Categories.category().getId();
        final var genreID = Fixture.Genres.genre().getId();
        final var castMemberID = Fixture.CastMembers.member().getId();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(categoryID.getValue());
        final var expectedGenres = Set.of(genreID.getValue());
        final var expectedMembers = Set.of(castMemberID.getValue());

        final var expectedVideo =
            new MockMultipartFile("video_file", "video.mp4", "video/mp4", "VIDEO".getBytes());
        final var expectedTrailer =
            new MockMultipartFile("trailer_file", "trailer.mp4", "video/mp4", "TRAILER".getBytes());
        final var expectedBanner =
            new MockMultipartFile("banner_file", "banner.jpg", "image/jpg", "BANNER".getBytes());
        final var expectedThumb =
            new MockMultipartFile("thumb_file", "thumb.jpg", "image/jpg", "THUMB".getBytes());
        final var expectedThumbHalf =
            new MockMultipartFile("thumb_half_file", "thumb_half.jpg", "image/jpg", "THUMB_HALF".getBytes());

        when(createVideoUseCase.execute(any()))
            .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        // when
        final var aRequest = multipart("/videos")
                .file(expectedVideo)
                .file(expectedTrailer)
                .file(expectedBanner)
                .file(expectedThumb)
                .file(expectedThumbHalf)
                .param("title", expectedTitle)
                .param("description", expectedDescription)
                .param("year_launched", String.valueOf(expectedLaunchYear.getValue()))
                .param("duration", String.valueOf(expectedDuration))
                .param("opened", String.valueOf(expectedOpened))
                .param("published", String.valueOf(expectedPublished))
                .param("rating", expectedRating.getName())
                .param("cast_members_id", castMemberID.getValue())
                .param("categories_id", categoryID.getValue())
                .param("genres_id", genreID.getValue())
                .accept(APPLICATION_JSON)
                .contentType(MULTIPART_FORM_DATA);

        mvc.perform(aRequest)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/videos/"+expectedId.getValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        // then
        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);
        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();
        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertEquals(expectedVideo.getOriginalFilename(), actualCmd.video().name());
        assertEquals(expectedTrailer.getOriginalFilename(), actualCmd.trailer().name());
        assertEquals(expectedBanner.getOriginalFilename(), actualCmd.banner().name());
        assertEquals(expectedThumb.getOriginalFilename(), actualCmd.thumbnail().name());
        assertEquals(expectedThumbHalf.getOriginalFilename(), actualCmd.thumbnailHalf().name());
    }

    @Test
    public void givenAValidCommand_whenCallsCreatePartial_thenShouldReturnAnId() throws Exception {
        // given
        final var categoryID = Fixture.Categories.category().getId();
        final var genreID = Fixture.Genres.genre().getId();
        final var castMemberID = Fixture.CastMembers.member().getId();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(categoryID.getValue());
        final var expectedGenres = Set.of(genreID.getValue());
        final var expectedMembers = Set.of(castMemberID.getValue());

        final var aCommand = new CreateVideoRequest(
            expectedTitle,
            expectedDescription,
            expectedDuration,
            expectedLaunchYear.getValue(),
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        when(createVideoUseCase.execute(any()))
            .thenReturn(new CreateVideoOutput(expectedId.getValue()));

        // when
        final var aRequest = post("/videos")
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(aRequest)
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/videos/"+expectedId.getValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        // then
        final var cmdCaptor = ArgumentCaptor.forClass(CreateVideoCommand.class);
        verify(createVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();
        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertTrue(actualCmd.getVideo().isEmpty());
        assertTrue(actualCmd.getTrailer().isEmpty());
        assertTrue(actualCmd.getBanner().isEmpty());
        assertTrue(actualCmd.getThumbnail().isEmpty());
        assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAValidId_whenCallsGetById_thenShouldReturnVideo() throws Exception {
        // given
        final var categoryID = Fixture.Categories.category().getId();
        final var genreID = Fixture.Genres.genre().getId();
        final var castMemberID = Fixture.CastMembers.member().getId();

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(categoryID.getValue());
        final var expectedGenres = Set.of(genreID.getValue());
        final var expectedMembers = Set.of(castMemberID.getValue());

        final var expectedVideo = Fixture.Videos.audioVideo(VideoMediaType.VIDEO);
        final var expectedTrailer = Fixture.Videos.audioVideo(VideoMediaType.TRAILER);
        final var expectedBanner = Fixture.Videos.image(VideoMediaType.BANNER);
        final var expectedThumb = Fixture.Videos.image(VideoMediaType.THUMBNAIL);
        final var expectedThumbHalf = Fixture.Videos.image(VideoMediaType.THUMBNAIL_HALF);

        final var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedRating,
            expectedOpened,
            expectedPublished,
            mapTo(expectedCategories, CategoryID::from),
            mapTo(expectedGenres, GenreID::from),
            mapTo(expectedMembers, CastMemberID::from)
        )
        .updatedVideoMedia(expectedVideo)
        .updateTrailerMedia(expectedTrailer)
        .updateBannerMedia(expectedBanner)
        .updateThumbnailMedia(expectedThumb)
        .updateThumbnailHalfMedia(expectedThumbHalf);

        final var expectedId = aVideo.getId().getValue();

        when(getVideoByIdUseCase.execute(any()))
            .thenReturn(VideoOutput.from(aVideo));

        // when
        final var aRequest = get("/videos/{id}",  expectedId)
                .accept(APPLICATION_JSON);

        final var response = mvc.perform(aRequest);

        // then
        response
            .andExpect(status().isOk())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId)))
            .andExpect(jsonPath("$.title", equalTo(expectedTitle)))
            .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
            .andExpect(jsonPath("$.year_launched", equalTo(expectedLaunchYear.getValue())))
            .andExpect(jsonPath("$.duration", equalTo(expectedDuration)))
            .andExpect(jsonPath("$.opened", equalTo(expectedOpened)))
            .andExpect(jsonPath("$.published", equalTo(expectedPublished)))
            .andExpect(jsonPath("$.rating", equalTo(expectedRating.getName())))
            .andExpect(jsonPath("$.created_at", equalTo(aVideo.getCreatedAt().toString())))
            .andExpect(jsonPath("$.updated_at", equalTo(aVideo.getUpdatedAt().toString())))
            .andExpect(jsonPath("$.categories_id", equalTo(new ArrayList<>(expectedCategories))))
            .andExpect(jsonPath("$.genres_id", equalTo(new ArrayList<>(expectedGenres))))
            .andExpect(jsonPath("$.cast_members_id", equalTo(new ArrayList<>(expectedMembers))))
            .andExpect(jsonPath("$.video.id", equalTo(expectedVideo.id())))
            .andExpect(jsonPath("$.video.checksum", equalTo(expectedVideo.checksum())))
            .andExpect(jsonPath("$.video.name", equalTo(expectedVideo.name())))
            .andExpect(jsonPath("$.video.location", equalTo(expectedVideo.rawLocation())))
            .andExpect(jsonPath("$.video.encoded_location", equalTo(expectedVideo.encodedLocation())))
            .andExpect(jsonPath("$.video.status", equalTo(expectedVideo.status().name())))
            .andExpect(jsonPath("$.trailer.id", equalTo(expectedTrailer.id())))
            .andExpect(jsonPath("$.trailer.checksum", equalTo(expectedTrailer.checksum())))
            .andExpect(jsonPath("$.trailer.name", equalTo(expectedTrailer.name())))
            .andExpect(jsonPath("$.trailer.location", equalTo(expectedTrailer.rawLocation())))
            .andExpect(jsonPath("$.trailer.encoded_location", equalTo(expectedTrailer.encodedLocation())))
            .andExpect(jsonPath("$.trailer.status", equalTo(expectedTrailer.status().name())))
            .andExpect(jsonPath("$.banner.id", equalTo(expectedBanner.id())))
            .andExpect(jsonPath("$.banner.name", equalTo(expectedBanner.name())))
            .andExpect(jsonPath("$.banner.location", equalTo(expectedBanner.location())))
            .andExpect(jsonPath("$.banner.checksum", equalTo(expectedBanner.checksum())))
            .andExpect(jsonPath("$.thumbnail.id", equalTo(expectedThumb.id())))
            .andExpect(jsonPath("$.thumbnail.name", equalTo(expectedThumb.name())))
            .andExpect(jsonPath("$.thumbnail.location", equalTo(expectedThumb.location())))
            .andExpect(jsonPath("$.thumbnail.checksum", equalTo(expectedThumb.checksum())))
            .andExpect(jsonPath("$.thumbnail_half.id", equalTo(expectedThumbHalf.id())))
            .andExpect(jsonPath("$.thumbnail_half.name", equalTo(expectedThumbHalf.name())))
            .andExpect(jsonPath("$.thumbnail_half.location", equalTo(expectedThumbHalf.location())))
            .andExpect(jsonPath("$.thumbnail_half.checksum", equalTo(expectedThumbHalf.checksum())));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateVideo_thenShouldReturnVideoID() throws Exception {
        // given
        final var categoryID = Fixture.Categories.category().getId();
        final var genreID = Fixture.Genres.genre().getId();
        final var castMemberID = Fixture.CastMembers.member().getId();

        final var expectedId = VideoID.unique();
        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(categoryID.getValue());
        final var expectedGenres = Set.of(genreID.getValue());
        final var expectedMembers = Set.of(castMemberID.getValue());

        final var aCommand = new UpdateVideoRequest(
            expectedTitle,
            expectedDescription,
            expectedDuration,
            expectedLaunchYear.getValue(),
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        when(updateVideoUseCase.execute(any()))
            .thenReturn(new UpdateVideoOutput(expectedId.getValue()));

        // when
        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(aRequest)
            .andExpect(status().isOk())
            .andExpect(header().string("Location", "/videos/"+expectedId.getValue()))
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id", equalTo(expectedId.getValue())));

        // then
        final var cmdCaptor = ArgumentCaptor.forClass(UpdateVideoCommand.class);
        verify(updateVideoUseCase).execute(cmdCaptor.capture());

        final var actualCmd = cmdCaptor.getValue();
        assertEquals(expectedTitle, actualCmd.title());
        assertEquals(expectedDescription, actualCmd.description());
        assertEquals(expectedLaunchYear.getValue(), actualCmd.launchedAt());
        assertEquals(expectedDuration, actualCmd.duration());
        assertEquals(expectedOpened, actualCmd.opened());
        assertEquals(expectedPublished, actualCmd.published());
        assertEquals(expectedRating.getName(), actualCmd.rating());
        assertEquals(expectedCategories, actualCmd.categories());
        assertEquals(expectedGenres, actualCmd.genres());
        assertEquals(expectedMembers, actualCmd.members());
        assertTrue(actualCmd.getVideo().isEmpty());
        assertTrue(actualCmd.getTrailer().isEmpty());
        assertTrue(actualCmd.getBanner().isEmpty());
        assertTrue(actualCmd.getThumbnail().isEmpty());
        assertTrue(actualCmd.getThumbnailHalf().isEmpty());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsUpdateVideo_thenShouldReturnNotification() throws Exception {
        // given
        final var categoryID = Fixture.Categories.category().getId();
        final var genreID = Fixture.Genres.genre().getId();
        final var castMemberID = Fixture.CastMembers.member().getId();

        final var expectedId = VideoID.unique();
        final var expectedTitle = "";
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(categoryID.getValue());
        final var expectedGenres = Set.of(genreID.getValue());
        final var expectedMembers = Set.of(castMemberID.getValue());
        final var expectedErrorMessage = "'title' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand = new UpdateVideoRequest(
            expectedTitle,
            expectedDescription,
            expectedDuration,
            expectedLaunchYear.getValue(),
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            expectedCategories,
            expectedGenres,
            expectedMembers
        );

        when(updateVideoUseCase.execute(any()))
            .thenThrow(NotificationException.with(new Error(expectedErrorMessage)));

        // when
        final var aRequest = put("/videos/{id}", expectedId.getValue())
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = mvc.perform(aRequest);

        // then
        response
            .andExpect(status().isUnprocessableEntity())
            .andExpect(header().string("Content-Type", APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
            .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
            .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateVideoUseCase).execute(any());
    }

    @Test
    public void givenAValidId_whenCallsDeleteById_thenShouldDeleteIt() throws Exception {
        // given
        final var expectedId = VideoID.unique();

        doNothing().when(deleteVideoUseCase).execute(any());

        // when
        final var aRequest = delete("/videos/{id}", expectedId.getValue());

        final var response = mvc.perform(aRequest);

        // then
        response.andExpect(status().isNoContent());

        verify(deleteVideoUseCase).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenValidParams_whenCallsListVideos_thenShouldReturnPagination() throws Exception {
        // given
        final var aVideo = VideoPreview.from(Fixture.video());

        final var expectedPage = 50;
        final var expectedPerPage = 50;
        final var expectedTerms = "Algo";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "cast1";
        final var expectedGenres = "gen1";
        final var expectedCategories = "cat1";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
            .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/videos")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .queryParam("cast_members_ids", expectedCastMembers)
                .queryParam("categories_ids", expectedCategories)
                .queryParam("genres_ids", expectedGenres)
                .accept(APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
            .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
            .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
            .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
            .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
            .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedDirection, actualQuery.direction());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedTerms, actualQuery.terms());
        assertEquals(Set.of(CategoryID.from(expectedCategories)), actualQuery.categories());
        assertEquals(Set.of(CastMemberID.from(expectedCastMembers)), actualQuery.castMembers());
        assertEquals(Set.of(GenreID.from(expectedGenres)), actualQuery.genres());
    }

    @Test
    public void givenEmptyParams_whenCallsListVideosWithDefaultValues_thenShouldReturnPagination() throws Exception {
        // given
        final var aVideo = VideoPreview.from(Fixture.video());

        final var expectedPage = 0;
        final var expectedPerPage = 25;
        final var expectedTerms = "";
        final var expectedSort = "title";
        final var expectedDirection = "asc";
        final var expectedCastMembers = "";
        final var expectedGenres = "";
        final var expectedCategories = "";

        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(VideoListOutput.from(aVideo));

        when(listVideosUseCase.execute(any()))
            .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var aRequest = get("/videos")
                .accept(APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
            .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
            .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
            .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
            .andExpect(jsonPath("$.items[0].id", equalTo(aVideo.id())))
            .andExpect(jsonPath("$.items[0].title", equalTo(aVideo.title())))
            .andExpect(jsonPath("$.items[0].description", equalTo(aVideo.description())))
            .andExpect(jsonPath("$.items[0].created_at", equalTo(aVideo.createdAt().toString())))
            .andExpect(jsonPath("$.items[0].updated_at", equalTo(aVideo.updatedAt().toString())));

        final var captor = ArgumentCaptor.forClass(VideoSearchQuery.class);

        verify(listVideosUseCase).execute(captor.capture());

        final var actualQuery = captor.getValue();
        assertEquals(expectedPage, actualQuery.page());
        assertEquals(expectedPerPage, actualQuery.perPage());
        assertEquals(expectedDirection, actualQuery.direction());
        assertEquals(expectedSort, actualQuery.sort());
        assertEquals(expectedTerms, actualQuery.terms());
        assertTrue(actualQuery.categories().isEmpty());
        assertTrue(actualQuery.castMembers().isEmpty());
        assertTrue(actualQuery.genres().isEmpty());
    }
}