package com.fullcycle.catalogo.admin.domain.event;

@FunctionalInterface
public interface DomainEventPublisher<T extends DomainEvent> {
    void publish(T event);
}
