package com.fullcycle.catalogo.admin.infrastructure.services.impl;

import com.fullcycle.catalogo.admin.domain.resource.Resource;
import com.fullcycle.catalogo.admin.infrastructure.services.StorageService;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class GoogleCloudStorageService implements StorageService {
    private final String bucket;
    private final Storage storage;

    public GoogleCloudStorageService(final String bucket, final Storage storage) {
        this.bucket = bucket;
        this.storage = storage;
    }


    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(storage.get(bucket, name))
                .map(blob -> Resource.of(
                    blob.getCrc32cToHexString(),
                    blob.getContent(),
                    blob.getContentType(),
                    name
                ));
    }

    @Override
    public void store(final String name, final Resource resource) {
        final var blobInfo = BlobInfo.newBuilder(bucket, name)
                .setContentType(resource.contentType())
                .setCrc32cFromHexString(resource.checksum())
                .build();

        storage.create(blobInfo, resource.content());
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        final var blobs = names.stream().map(s -> BlobId.of(bucket, s)).toList();
        storage.delete(blobs);
    }

    @Override
    public List<String> list(final String prefix) {
        final var blobs = storage.list(bucket, Storage.BlobListOption.prefix(prefix));
        return StreamSupport.stream(blobs.iterateAll().spliterator(), false)
                .map(BlobInfo::getBlobId)
                .map(BlobId::getName)
                .toList();
    }
}
