package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.video.media.AudioVideoMedia;
import com.fullcycle.catalogo.admin.domain.video.media.MediaStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AudioVideoMediaTest {

    @Test
    public void givenValidParams_whenCallsNewAudioVideo_thenShouldReturnInstance() {
        // given
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedRawLocation = "/images/abc";
        final var expectedEncodedLocation = "/images/encoded";
        final var expectedStatus = MediaStatus.PENDING;

        // when
        final var actualVideoMedia = AudioVideoMedia.with(
            expectedChecksum,
            expectedName,
            expectedRawLocation,
            expectedEncodedLocation,
            expectedStatus
        );

        // then
        assertNotNull(actualVideoMedia);
        assertEquals(expectedChecksum, actualVideoMedia.checksum());
        assertEquals(expectedName, actualVideoMedia.name());
        assertEquals(expectedRawLocation, actualVideoMedia.rawLocation());
        assertEquals(expectedEncodedLocation, actualVideoMedia.encodedLocation());
        assertEquals(expectedStatus, actualVideoMedia.status());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_thenShouldReturnTrue() {
        // given
        final var expectedChecksum = "abc";
        final var expectedRawLocation = "/images/abc";

        final var img1 = AudioVideoMedia.with(
            expectedChecksum,
            "random",
            expectedRawLocation,
            "",
            MediaStatus.PENDING
        );

        final var img2 = AudioVideoMedia.with(
            expectedChecksum,
            "simple",
            expectedRawLocation,
            "",
            MediaStatus.PENDING
        );

        // then
        assertEquals(img1, img2);
        assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_thenShouldReturnError() {
        assertThrows(
            NullPointerException.class,
            () -> AudioVideoMedia.with(null, "random", "/videos", "/videos", MediaStatus.PENDING)
        );

        assertThrows(
            NullPointerException.class,
            () -> AudioVideoMedia.with("abc", null, "/videos", "/videos", MediaStatus.PENDING)
        );

        assertThrows(
            NullPointerException.class,
            () -> AudioVideoMedia.with("abc", "random", null, "/videos", MediaStatus.PENDING)
        );

        assertThrows(
            NullPointerException.class,
            () -> AudioVideoMedia.with("abc", "random", "/videos", null, MediaStatus.PENDING)
        );

        assertThrows(
            NullPointerException.class,
            () -> AudioVideoMedia.with("abc", "random", "/videos", "/videos", null)
        );
    }

}