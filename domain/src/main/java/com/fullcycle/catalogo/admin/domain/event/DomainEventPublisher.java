package com.fullcycle.catalogo.admin.domain.event;

@FunctionalInterface
public interface DomainEventPublisher {
    void publish(DomainEvent event);
}
