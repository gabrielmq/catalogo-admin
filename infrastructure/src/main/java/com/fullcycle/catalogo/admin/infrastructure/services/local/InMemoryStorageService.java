package com.fullcycle.catalogo.admin.infrastructure.services.local;

import com.fullcycle.catalogo.admin.domain.resource.Resource;
import com.fullcycle.catalogo.admin.infrastructure.services.StorageService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorageService implements StorageService {
    private final Map<String, Resource> storage;

    public InMemoryStorageService() {
        this.storage = new ConcurrentHashMap<>();
    }

    public Map<String, Resource> getStorage() {
        return storage;
    }

    public void reset() {
        storage.clear();
    }

    @Override
    public Optional<Resource> get(final String name) {
        return Optional.ofNullable(storage.get(name));
    }

    @Override
    public void store(final String name, final Resource resource) {
        storage.put(name, resource);
    }

    @Override
    public void deleteAll(final Collection<String> names) {
        names.forEach(storage::remove);
    }

    @Override
    public List<String> list(final String prefix) {
        return Objects.nonNull(prefix)
                ? storage.keySet().stream().filter(s -> s.startsWith(prefix)).toList()
                : Collections.emptyList();
    }
}
