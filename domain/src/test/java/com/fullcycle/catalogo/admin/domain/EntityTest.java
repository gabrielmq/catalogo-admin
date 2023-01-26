package com.fullcycle.catalogo.admin.domain;

import com.fullcycle.catalogo.admin.domain.event.DomainEvent;
import com.fullcycle.catalogo.admin.domain.utils.IDUtils;
import com.fullcycle.catalogo.admin.domain.utils.InstantUtils;
import com.fullcycle.catalogo.admin.domain.validation.ValidationHandler;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class EntityTest extends UnitTest {

    @Test
    public void givenNullAsEvent_whenInstantiate_thenShouldBeOk() {
        // given
        final List<DomainEvent> events = null;

        // when
        final var anEntity = new DummyEntity(new DummyID(), events);

        // then
        assertNotNull(anEntity.getDomainEvents());
        assertTrue(anEntity.getDomainEvents().isEmpty());
    }

    @Test
    public void givenDomainEvents_whenPassInConstructor_thenShouldCreateADefensiveClone() {
        // given
        final List<DomainEvent> events = new ArrayList<>();
        events.add((DomainEvent) () -> null);

        // when
        final var anEntity = new DummyEntity(new DummyID(), events);

        // then
        assertNotNull(anEntity.getDomainEvents());
        assertEquals(1, anEntity.getDomainEvents().size());
        assertThrows(RuntimeException.class, () -> {
            final var actualEvents = anEntity.getDomainEvents();
            actualEvents.add(new DummyEvent());
        });
    }

    @Test
    public void givenEmptyDomainEvents_whenCallRegisterEvent_thenShouldAddEventToList() {
        // given
        final List<DomainEvent> events = new ArrayList<>();
        final var anEntity = new DummyEntity(new DummyID(), events);

        final var expectedEvents = 1;

        // when
        anEntity.registerEvent(new DummyEvent());

        // then
        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        assertThrows(RuntimeException.class, () -> {
            final var actualEvents = anEntity.getDomainEvents();
            actualEvents.add(new DummyEvent());
        });
    }

    @Test
    public void givenAFewDomainEvents_whenCallPublishEvents_thenShouldCallPublishAndClearTheList() {
        // given
        final var expectedEvents = 0;
        final var expectedSentEvent = 2;
        final var counter = new AtomicInteger(0);

        final List<DomainEvent> events = new ArrayList<>();
        final var anEntity = new DummyEntity(new DummyID(), events);
        anEntity.registerEvent(new DummyEvent());
        anEntity.registerEvent(new DummyEvent());

        assertEquals(2, anEntity.getDomainEvents().size());

        // when
        anEntity.publishDomainEvents(event -> counter.incrementAndGet());

        // then
        assertNotNull(anEntity.getDomainEvents());
        assertEquals(expectedEvents, anEntity.getDomainEvents().size());
        assertEquals(expectedSentEvent, counter.get());
    }

    public static class DummyEvent implements DomainEvent {
        @Override
        public Instant occurredOn() {
            return InstantUtils.now();
        }
    }

    public static class DummyID extends Identifier {

        private final String id;

        public DummyID() {
            this.id = IDUtils.uuid();
        }


        @Override
        public String getValue() {
            return id;
        }
    }
    public static class DummyEntity extends Entity<DummyID> {
        public DummyEntity(final DummyID dummyID, final List<DomainEvent> events) {
            super(dummyID, events);
        }

        @Override
        public void validate(final ValidationHandler handler) {}
    }
}
