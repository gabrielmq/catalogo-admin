package com.fullcycle.catalogo.admin.domain.validation;

import java.util.List;
import java.util.Objects;

public interface ValidationHandler {
    ValidationHandler append(Error anError);
    ValidationHandler append(ValidationHandler anHandler);
    ValidationHandler validate(Validation aValidation);
    List<Error> getErrors();

    default boolean hasErrors() {
        return Objects.nonNull(getErrors()) && !getErrors().isEmpty();
    }

    default Error getFirstError() {
        if (Objects.nonNull(getErrors()) && !getErrors().isEmpty()) {
            return getErrors().get(0);
        }
        return null;
    }

    @FunctionalInterface
    interface Validation {
        void validate();
    }
}
