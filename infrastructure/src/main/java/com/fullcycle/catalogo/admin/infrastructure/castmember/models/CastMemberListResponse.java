package com.fullcycle.catalogo.admin.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("type") CastMemberType type,
    @JsonProperty("created_at") Instant createdAt
) {
}
