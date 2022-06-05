package com.fullcycle.catalogo.admin.application.category.create;

import com.fullcycle.catalogo.admin.application.UseCase;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
