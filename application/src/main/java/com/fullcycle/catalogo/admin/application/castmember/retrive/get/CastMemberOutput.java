package com.fullcycle.catalogo.admin.application.castmember.retrive.get;

import com.fullcycle.catalogo.admin.domain.castmember.CastMember;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt,
    Instant updatedAt
) {
    public static CastMemberOutput from(final CastMember aMember) {
        return new CastMemberOutput(
            aMember.getId().getValue(),
            aMember.getName(),
            aMember.getType(),
            aMember.getCreatedAt(),
            aMember.getUpdatedAt()
        );
    }
}
