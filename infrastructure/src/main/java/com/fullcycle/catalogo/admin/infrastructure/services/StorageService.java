package com.fullcycle.catalogo.admin.infrastructure.services;

import com.fullcycle.catalogo.admin.domain.resource.Resource;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StorageService {
    Optional<Resource> get(String name);
    void store(String name, Resource resource);
    void deleteAll(Collection<String> names);
    List<String> list(String prefix);
}
