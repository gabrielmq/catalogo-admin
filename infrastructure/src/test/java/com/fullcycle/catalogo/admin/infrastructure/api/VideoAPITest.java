package com.fullcycle.catalogo.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.catalogo.admin.ControllerTest;
import com.fullcycle.catalogo.admin.application.video.create.CreateVideoCommand;
import com.fullcycle.catalogo.admin.application.video.create.CreateVideoOutput;
import com.fullcycle.catalogo.admin.application.video.create.CreateVideoUseCase;
import com.fullcycle.catalogo.admin.domain.Fixture;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.infrastructure.video.models.CreateVideoRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Year;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = VideoAPI.class)
public class VideoAPITest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateVideoUseCase createVideoUseCase;

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
}