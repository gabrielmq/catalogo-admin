package com.fullcycle.catalogo.admin.infrastructure.castmember.models;

import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(
    String name,
    CastMemberType type
) {
}
