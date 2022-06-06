package com.fullcycle.catalogo.admin.application.category.update;

import com.fullcycle.catalogo.admin.application.UseCase;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
