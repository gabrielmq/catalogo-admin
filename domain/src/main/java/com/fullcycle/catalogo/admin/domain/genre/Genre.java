package com.fullcycle.catalogo.admin.domain.genre;

import com.fullcycle.catalogo.admin.domain.AggregateRoot;
import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.utils.InstantUtils;
import com.fullcycle.catalogo.admin.domain.validation.ValidationHandler;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Genre extends AggregateRoot<GenreID> {
    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(
        final GenreID anId,
        final String aName,
        final boolean isActive,
        final List<CategoryID> categories,
        final Instant aCreationDate,
        final Instant aUpdateDate,
        final Instant aDeleteDate
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.deletedAt = aDeleteDate;

        selfValidation();
    }

    private void selfValidation() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate Genre", notification);
        }
    }

    public static Genre newGenreWith(final String aName, final boolean isActive) {
        final var id = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(id, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    public static Genre with(
        final GenreID anId,
        final String aName,
        final boolean isActive,
        final List<CategoryID> categories,
        final Instant aCreationDate,
        final Instant aUpdateDate,
        final Instant aDeleteDate
    ) {
        return new Genre(anId, aName, isActive, categories, aCreationDate, aUpdateDate, aDeleteDate);
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(
            aGenre.id,
            aGenre.name,
            aGenre.active,
            new ArrayList<>(aGenre.categories),
            aGenre.createdAt,
            aGenre.updatedAt,
            aGenre.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        this.name = aName;
        this.categories = new ArrayList<>(Objects.requireNonNullElse(categories, Collections.emptyList()));
        this.updatedAt = InstantUtils.now();
        selfValidation();
        return this;
    }

    public Genre deactivate() {
        if (Objects.isNull(getDeletedAt())) {
            this.deletedAt = InstantUtils.now();
        }
        this.active = false;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if (Objects.isNull(aCategoryID)) {
            return this;
        }

        this.categories.add(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategories(final List<CategoryID> categories) {
        if (Objects.isNull(categories) || categories.isEmpty()) {
            return this;
        }

        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID) {
        if (Objects.isNull(aCategoryID)) {
            return this;
        }
        this.categories.remove(aCategoryID);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
