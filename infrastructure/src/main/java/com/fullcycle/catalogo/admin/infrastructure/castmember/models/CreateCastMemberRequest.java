package com.fullcycle.catalogo.admin.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(
    @JsonProperty("name") String name,
    @JsonProperty("type") CastMemberType type
) {
}
