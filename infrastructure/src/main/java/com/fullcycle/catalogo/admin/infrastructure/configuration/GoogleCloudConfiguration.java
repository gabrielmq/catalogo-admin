package com.fullcycle.catalogo.admin.infrastructure.configuration;

import com.fullcycle.catalogo.admin.infrastructure.configuration.properties.google.GoogleCloudProperties;
import com.fullcycle.catalogo.admin.infrastructure.configuration.properties.google.GoogleStorageProperties;
import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.threeten.bp.Duration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
@Profile({ "dev", "prod" })
public class GoogleCloudConfiguration {
    @Bean
    @ConfigurationProperties("gcloud")
    public GoogleCloudProperties googleCloudProperties() {
        return new GoogleCloudProperties();
    }

    @Bean
    @ConfigurationProperties("gcloud.storage.catalogo-video")
    public GoogleStorageProperties googleStorageProperties() {
        return new GoogleStorageProperties();
    }

    @Bean
    public Credentials credentials(final GoogleCloudProperties props) {
        final var content = Base64.getDecoder().decode(props.getCredentials());
        try (final var stream = new ByteArrayInputStream(content)) {
            return GoogleCredentials.fromStream(stream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Storage storage(final Credentials credentials, final GoogleStorageProperties storageProps) {
        final var transportOpts = HttpTransportOptions.newBuilder()
                .setConnectTimeout(storageProps.getConnectTimeout())
                .setReadTimeout(storageProps.getReadTimeout())
                .build();

        final var retrySettings = RetrySettings.newBuilder()
                .setInitialRetryDelay(Duration.ofMillis(storageProps.getRetryDelay()))
                .setMaxRetryDelay(Duration.ofMillis(storageProps.getRetryMaxDelay()))
                .setMaxAttempts(storageProps.getRetryMaxAttempts())
                .setRetryDelayMultiplier(storageProps.getRetryMultiplier())
                .build();

        return StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setTransportOptions(transportOpts)
                .setRetrySettings(retrySettings)
                .build()
                .getService();
    }
}
