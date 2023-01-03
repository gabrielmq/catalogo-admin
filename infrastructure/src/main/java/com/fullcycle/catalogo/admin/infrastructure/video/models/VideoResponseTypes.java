package com.fullcycle.catalogo.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
@JacksonAnnotationsInside
@JsonSubTypes({
    @JsonSubTypes.Type(value = VideoEncoderCompleted.class),
    @JsonSubTypes.Type(value = VideoEncoderError.class)
})
public @interface VideoResponseTypes {
}
