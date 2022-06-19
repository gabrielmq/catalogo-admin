package com.fullcycle.catalogo.admin.application.genre.create;

import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.genre.Genre;
import com.fullcycle.catalogo.admin.domain.genre.GenreGateway;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.validation.ValidationHandler;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {
    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public DefaultCreateGenreUseCase(
        final GenreGateway genreGateway,
        final CategoryGateway categoryGateway
    ) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryID(aCommand.categories());

        final var notification = Notification.create();
        notification.append(validateCategories(categories));

        final var aGenre = notification.validate(() -> Genre.newGenreWith(aName, isActive));
        if (notification.hasErrors()) {
            throw new NotificationException("Could not create aggregate Genre", notification);
        }

        aGenre.addCategories(categories);
        return CreateGenreOutput.from(genreGateway.create(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);
        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }
        return notification;
    }

    private List<CategoryID> toCategoryID(final List<String> categories) {
        return categories.stream().map(CategoryID::from).toList();
    }
}
