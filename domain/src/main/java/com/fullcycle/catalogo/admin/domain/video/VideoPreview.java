package com.fullcycle.catalogo.admin.domain.video;

import java.time.Instant;

public record VideoPreview(
    String id,
    String description,
    String title,
    Instant createdAt,
    Instant updatedAt
) {
}
