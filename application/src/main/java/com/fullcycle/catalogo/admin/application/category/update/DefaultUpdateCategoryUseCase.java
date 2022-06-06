package com.fullcycle.catalogo.admin.application.category.update;

import com.fullcycle.catalogo.admin.domain.category.Category;
import com.fullcycle.catalogo.admin.domain.category.CategoryGateway;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.exceptions.DomainException;
import com.fullcycle.catalogo.admin.domain.validation.Error;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
        final var anId = CategoryID.from(aCommand.id());
        final var aCategory = categoryGateway.findById(anId).orElseThrow(notFound(anId));

        final var notification = Notification.create();
        aCategory
            .update(aCommand.name(), aCommand.description(), aCommand.isActive())
            .validate(notification);

        return notification.hasErrors() ? Left(notification) : update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return Try(() -> categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private Supplier<DomainException> notFound(final CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }
}
