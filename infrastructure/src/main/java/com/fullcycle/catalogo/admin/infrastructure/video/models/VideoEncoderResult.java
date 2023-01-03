package com.fullcycle.catalogo.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@VideoResponseTypes
@JsonTypeInfo(use = NAME, include = EXISTING_PROPERTY, property = "status")
public sealed interface VideoEncoderResult
    permits VideoEncoderCompleted, VideoEncoderError {
    String getStatus();
}
