package com.fullcycle.catalogo.admin.infrastructure.configuration;

import com.fullcycle.catalogo.admin.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.fullcycle.catalogo.admin.infrastructure.configuration.properties.storage.StorageProperties;
import com.fullcycle.catalogo.admin.infrastructure.services.StorageService;
import com.fullcycle.catalogo.admin.infrastructure.services.impl.GoogleCloudStorageService;
import com.fullcycle.catalogo.admin.infrastructure.services.local.InMemoryStorageService;
import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration(proxyBeanMethods = false)
public class StorageConfiguration {
    @Bean
    @ConfigurationProperties("storage.catalogo-videos")
    public StorageProperties storageProperties() {
        return new StorageProperties();
    }

    @Bean
    @Profile({ "dev", "test-integration", "test-e2e" })
    public StorageService localStorageAPI() {
        return new InMemoryStorageService();
    }

    @Bean
    @ConditionalOnMissingBean
    public StorageService gcloudStorageService(final GoogleStorageProperties props, final Storage storage) {
        return new GoogleCloudStorageService(props.getBucket(), storage);
    }
}
