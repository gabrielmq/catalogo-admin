package com.fullcycle.catalogo.admin.infrastructure.services.local;

import com.fullcycle.catalogo.admin.domain.Fixture;
import com.fullcycle.catalogo.admin.domain.utils.IDUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.fullcycle.catalogo.admin.domain.video.VideoMediaType.VIDEO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InMemoryStorageServiceTest {
    private InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setup() {
        target.reset();
    }

    @Test
    public void givenValidResource_whenCallsStore_thenShouldStoreIt() {
        // given
        final var expectedResource = Fixture.Videos.resource(VIDEO);
        final var expectedName = IDUtils.uuid();

        // when
        target.store(expectedName, expectedResource);

        // then
        assertEquals(expectedResource, target.getStorage().get(expectedName));
    }

    @Test
    public void givenValidResource_whenCallsGet_thenShouldRetrieveIt() {
        // given
        final var expectedResource = Fixture.Videos.resource(VIDEO);
        final var expectedName = IDUtils.uuid();

        target.store(expectedName, expectedResource);

        // when
        final var actualResource = target.get(expectedName).get();

        // then
        assertEquals(expectedResource, actualResource);
    }

    @Test
    public void givenInvalidResource_whenCallsGet_thenShouldBeEmpty() {
        // given
        final var expectedName = IDUtils.uuid();

        // when
        final var actualResource = target.get(expectedName);

        // then
        assertTrue(actualResource.isEmpty());
    }

    @Test
    public void givenValidPrefix_whenCallsList_thenShouldRetrieveAll() {
        // given
        final var expectedNames = List.of(
            "video_" + IDUtils.uuid(),
            "video_" + IDUtils.uuid(),
            "video_" + IDUtils.uuid()
        );

        final var all = new ArrayList<>(expectedNames);
        all.add("image_" + IDUtils.uuid());
        all.add("image_" + IDUtils.uuid());

        all.forEach(name -> target.store(name, Fixture.Videos.resource(VIDEO)));

        assertEquals(5, all.size());

        // when
        final var actualResource = target.list("video");

        // then
        assertTrue(
            expectedNames.size() == actualResource.size() && expectedNames.containsAll(actualResource)
        );
    }

    @Test
    public void givenValidNames_whenCallsDelete_thenShouldDeleteAll() {
        // given
        final var videos = List.of(
            "video_" + IDUtils.uuid(),
            "video_" + IDUtils.uuid(),
            "video_" + IDUtils.uuid()
        );

        final var images = Set.of(
            "image_" + IDUtils.uuid(),
            "image_" + IDUtils.uuid()
        );

        final var all = new ArrayList<>(videos);
        all.addAll(images);

        all.forEach(name -> target.store(name, Fixture.Videos.resource(VIDEO)));

        assertEquals(5, all.size());

        // when
        target.deleteAll(videos);

        // then
        assertEquals(2, target.getStorage().size());
        assertEquals(images, target.getStorage().keySet());
    }
}