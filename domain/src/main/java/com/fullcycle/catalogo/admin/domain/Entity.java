package com.fullcycle.catalogo.admin.domain;

import com.fullcycle.catalogo.admin.domain.event.DomainEvent;
import com.fullcycle.catalogo.admin.domain.event.DomainEventPublisher;
import com.fullcycle.catalogo.admin.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> {
    protected final ID id;
    private final List<DomainEvent> events;

    protected Entity(final ID id, final List<DomainEvent> events) {
        this.id = Objects.requireNonNull(id, "'id' should not be null");
        this.events = new ArrayList<>(Objects.requireNonNullElse(events, Collections.emptyList()));
    }

    public abstract void validate(ValidationHandler handler);

    public ID getId() {
        return id;
    }

    public List<DomainEvent> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if (Objects.nonNull(publisher)) {
            getEvents().forEach(publisher::publish);
            events.clear();
        }
    }

    public void registerEvent(final DomainEvent event) {
        if (Objects.nonNull(event)) {
            events.add(event);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
