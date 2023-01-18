package com.fullcycle.catalogo.admin.domain.video;

import com.fullcycle.catalogo.admin.domain.UnitTest;
import com.fullcycle.catalogo.admin.domain.video.media.ImageMedia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImageMediaTest extends UnitTest {

    @Test
    public void givenValidParams_whenCallsNewImage_thenShouldReturnInstance() {
        // given
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedLocation = "/images/abc";

        // when
        final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        // then
        assertNotNull(actualImage);
        assertEquals(expectedChecksum, actualImage.checksum());
        assertEquals(expectedName, actualImage.name());
        assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_thenShouldReturnTrue() {
        // given
        final var expectedChecksum = "abc";
        final var expectedLocation = "/images/abc";

        final var img1 = ImageMedia.with(expectedChecksum, "random", expectedLocation);
        final var img2 = ImageMedia.with(expectedChecksum, "simple", expectedLocation);

        // then
        assertEquals(img1, img2);
        assertNotSame(img1, img2);
    }

    @Test
    public void givenInvalidParams_whenCallsWith_thenShouldReturnError() {
        assertThrows(NullPointerException.class, () -> ImageMedia.with(null, "random", "/images"));
        assertThrows(NullPointerException.class, () -> ImageMedia.with("abc", null, "/images"));
        assertThrows(NullPointerException.class, () -> ImageMedia.with("abc", "random", null));
    }
}