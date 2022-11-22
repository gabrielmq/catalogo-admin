package com.fullcycle.catalogo.admin.infrastructure.video;

import com.fullcycle.catalogo.admin.IntegrationTest;
import com.fullcycle.catalogo.admin.domain.video.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import com.fullcycle.catalogo.admin.domain.video.VideoMediaType;
import com.fullcycle.catalogo.admin.domain.video.VideoResource;
import com.fullcycle.catalogo.admin.domain.video.media.MediaStatus;
import com.fullcycle.catalogo.admin.infrastructure.services.StorageService;
import com.fullcycle.catalogo.admin.infrastructure.services.local.InMemoryStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static com.fullcycle.catalogo.admin.domain.Fixture.Videos.mediaType;
import static com.fullcycle.catalogo.admin.domain.Fixture.Videos.resource;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class DefaultMediaResourceGatewayTest {
    @Autowired
    private StorageService storageService;

    @Autowired
    private MediaResourceGateway gateway;

    @BeforeEach
    public void setUp() {
        storageService().reset();
    }

    @Test
    public void testInjection() {
        assertNotNull(gateway);
        assertInstanceOf(DefaultMediaResourceGateway.class, gateway);

        assertNotNull(storageService);
        assertInstanceOf(InMemoryStorageService.class, storageService);
    }

    @Test
    public void givenValidResource_whenCallsStorageAudioVideo_thenShouldStoreIt() {
        // given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
        final var expectedStatus = MediaStatus.PENDING;
        final var expectedEncodedLocation = "";

        // when
        final var actualMedia =
            this.gateway.storeAudioVideo(expectedVideoId, VideoResource.of(expectedResource, expectedType));

        // then
        assertNotNull(actualMedia.id());
        assertEquals(expectedLocation, actualMedia.rawLocation());
        assertEquals(expectedResource.name(), actualMedia.name());
        assertEquals(expectedResource.checksum(), actualMedia.checksum());
        assertEquals(expectedStatus, actualMedia.status());
        assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());

        final var actualStored = storageService().getStorage().get(expectedLocation);

        assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidResource_whenCallsStorageImage_thenShouldStoreIt() {
        // given
        final var expectedVideoId = VideoID.unique();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = resource(expectedType);
        final var expectedLocation = "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

        // when
        final var actualMedia =
            this.gateway.storeImage(expectedVideoId, VideoResource.of(expectedResource, expectedType));

        // then
        assertNotNull(actualMedia.id());
        assertEquals(expectedLocation, actualMedia.location());
        assertEquals(expectedResource.name(), actualMedia.name());
        assertEquals(expectedResource.checksum(), actualMedia.checksum());

        final var actualStored = storageService().getStorage().get(expectedLocation);

        assertEquals(expectedResource, actualStored);
    }

    @Test
    public void givenValidVideoId_whenCallsClearResources_thenShouldDeleteAll() {
        // given
        final var videoOne = VideoID.unique();
        final var videoTwo = VideoID.unique();

        final var toBeDeleted = new ArrayList<String>();
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()));
        toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()));

        final var expectedValues = new ArrayList<String>();
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.VIDEO.name()));
        expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VideoMediaType.BANNER.name()));

        toBeDeleted.forEach(id -> storageService().store(id, resource(mediaType())));
        expectedValues.forEach(id -> storageService().store(id, resource(mediaType())));

        assertEquals(5, storageService().getStorage().size());

        // when
        this.gateway.clearResources(videoOne);

        // then
        assertEquals(2, storageService().getStorage().size());

        final var actualKeys = storageService().getStorage().keySet();

        Assertions.assertTrue(
        expectedValues.size() == actualKeys.size()
                && actualKeys.containsAll(expectedValues)
        );
    }

    @Test
    public void givenValidVideoId_whenCallsGetResources_thenShouldReturnIt() {
        // given
        final var videoOne = VideoID.unique();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = resource(expectedType);

        storageService()
            .store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()), expectedResource);

        storageService()
            .store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()), resource(mediaType()));

        storageService()
            .store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()), resource(mediaType()));

        assertEquals(3, storageService().getStorage().size());

        // when
        final var actualResult = this.gateway.getResource(videoOne, expectedType).get();

        // then
        assertEquals(expectedResource, actualResult);
    }

    @Test
    public void givenInvalidType_whenCallsGetResources_thenShouldReturnEmpty() {
        // given
        final var videoOne = VideoID.unique();
        final var expectedType = VideoMediaType.THUMBNAIL;

        storageService()
            .store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.VIDEO.name()), resource(mediaType()));

        storageService()
            .store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.TRAILER.name()), resource(mediaType()));

        storageService()
            .store("videoId-%s/type-%s".formatted(videoOne.getValue(), VideoMediaType.BANNER.name()), resource(mediaType()));

        assertEquals(3, storageService().getStorage().size());

        // when
        final var actualResult = this.gateway.getResource(videoOne, expectedType);

        // then
        assertTrue(actualResult.isEmpty());
    }

    private InMemoryStorageService storageService() {
        return (InMemoryStorageService) storageService;
    }
}