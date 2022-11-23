package com.fullcycle.catalogo.admin.infrastructure.configuration;

import com.fullcycle.catalogo.admin.infrastructure.configuration.annontations.VideoCreatedQueue;
import com.fullcycle.catalogo.admin.infrastructure.configuration.annontations.VideoEncodedQueue;
import com.fullcycle.catalogo.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AMQPConfiguration {

    @Bean
    @VideoCreatedQueue
    @ConfigurationProperties("amqp.queues.video-created")
    public QueueProperties videoCreatedQueueProperties() {
        return new QueueProperties();
    }

    @Bean
    @VideoEncodedQueue
    @ConfigurationProperties("amqp.queues.video-encoded")
    public QueueProperties videoEncodedQueueProperties() {
        return new QueueProperties();
    }
}
