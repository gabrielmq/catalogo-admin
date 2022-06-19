package com.fullcycle.catalogo.admin.application.genre.create;

import com.fullcycle.catalogo.admin.application.UseCase;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryCommand;
import com.fullcycle.catalogo.admin.application.category.create.CreateCategoryOutput;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateGenreUseCase
        extends UseCase<CreateGenreCommand, CreateGenreOutput> {
}
