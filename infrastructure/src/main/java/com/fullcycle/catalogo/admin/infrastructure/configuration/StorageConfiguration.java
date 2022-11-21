package com.fullcycle.catalogo.admin.infrastructure.configuration;

import com.fullcycle.catalogo.admin.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.fullcycle.catalogo.admin.infrastructure.services.StorageService;
import com.fullcycle.catalogo.admin.infrastructure.services.impl.GoogleCloudStorageService;
import com.fullcycle.catalogo.admin.infrastructure.services.local.InMemoryStorageService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class StorageConfiguration {
    @Bean("storageService")
    @Profile({ "dev", "prod" })
    public StorageService gcloudStorageService(final GoogleStorageProperties props, final Storage storage) {
        return new GoogleCloudStorageService(props.getBucket(), storage);
    }

    @Bean("storageService")
    @ConditionalOnMissingBean
    public StorageService inMemoryStorageService() {
        return new InMemoryStorageService();
    }
}
