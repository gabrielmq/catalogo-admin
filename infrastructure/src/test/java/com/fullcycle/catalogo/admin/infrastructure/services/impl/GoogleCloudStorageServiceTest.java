package com.fullcycle.catalogo.admin.infrastructure.services.impl;

import com.fullcycle.catalogo.admin.domain.Fixture;
import com.fullcycle.catalogo.admin.domain.resource.Resource;
import com.fullcycle.catalogo.admin.domain.video.VideoMediaType;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class GoogleCloudStorageServiceTest {
    private GoogleCloudStorageService target;
    private Storage storage;
    private final String bucket = "test";

    @BeforeEach
    public void setup() {
        this.storage = mock(Storage.class);
        this.target = new GoogleCloudStorageService(bucket, storage);
    }

    @Test
    public void givenValidResource_whenCallsStore_thenShouldStoreIt() {
        // given
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        final var blob = mockBlob(expectedResource);
        doReturn(blob).when(storage).get(eq(bucket), eq(expectedId));

        // when
        this.target.store(expectedId, expectedResource);

        // then
        final var capturer = ArgumentCaptor.forClass(BlobInfo.class);

        verify(storage, times(1))
            .create(capturer.capture(), eq(expectedResource.content()));

        final var actualBlob = capturer.getValue();
        Assertions.assertEquals(this.bucket, actualBlob.getBlobId().getBucket());
        Assertions.assertEquals(expectedId, actualBlob.getBlobId().getName());
        Assertions.assertEquals(expectedResource.contentType(), actualBlob.getContentType());
        Assertions.assertEquals(expectedResource.checksum(), actualBlob.getCrc32cToHexString());
    }

    @Test
    public void givenResource_whenCallsGet_thenShouldRetrieveIt() {
        // given
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = expectedResource.name();

        final Blob blob = mockBlob(expectedResource);
        doReturn(blob).when(storage).get(eq(bucket), eq(expectedId));

        // when
        final var actualContent = target.get(expectedId).get();

        // then
        Assertions.assertEquals(expectedResource.checksum(), actualContent.checksum());
        Assertions.assertEquals(expectedResource.name(), actualContent.name());
        Assertions.assertEquals(expectedResource.content(), actualContent.content());
        Assertions.assertEquals(expectedResource.contentType(), actualContent.contentType());
    }

    @Test
    public void givenPrefix_whenCallsList_thenShouldRetrieveAll() {
        // given
        final var video = Fixture.Videos.resource(VideoMediaType.VIDEO);
        final var banner = Fixture.Videos.resource(VideoMediaType.BANNER);
        final var expectedIds = List.of(video.name(), banner.name());

        final var page = mock(Page.class);

        final Blob blob1 = mockBlob(video);
        final Blob blob2 = mockBlob(banner);

        doReturn(List.of(blob1, blob2)).when(page).iterateAll();
        doReturn(page).when(storage).list(eq(bucket), eq(Storage.BlobListOption.prefix("it")));

        // when
        final var actualContent = target.list("it");

        // then
        Assertions.assertTrue(
            expectedIds.size() == actualContent.size() && expectedIds.containsAll(actualContent)
        );
    }

    @Test
    public void givenResource_whenCallsDeleteAll_thenShouldEmptyStorage() {
        // given
        final var expectedIds = List.of("item1", "item2");

        // when
        target.deleteAll(expectedIds);

        // then
        final var capturer = ArgumentCaptor.forClass(List.class);

        verify(storage, times(1)).delete(capturer.capture());

        final var actualIds = ((List<BlobId>) capturer.getValue()).stream()
                .map(BlobId::getName)
                .toList();

        Assertions.assertTrue(expectedIds.size() == actualIds.size() && actualIds.containsAll(expectedIds));
    }

    private Blob mockBlob(final Resource resource) {
        final var blob1 = mock(Blob.class);
        when(blob1.getBlobId()).thenReturn(BlobId.of(bucket, resource.name()));
        when(blob1.getCrc32cToHexString()).thenReturn(resource.checksum());
        when(blob1.getContent()).thenReturn(resource.content());
        when(blob1.getContentType()).thenReturn(resource.contentType());
        when(blob1.getName()).thenReturn(resource.name());
        return blob1;
    }
}