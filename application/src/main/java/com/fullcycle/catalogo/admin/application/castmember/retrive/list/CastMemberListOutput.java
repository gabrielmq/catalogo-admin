package com.fullcycle.catalogo.admin.application.castmember.retrive.list;

import com.fullcycle.catalogo.admin.domain.castmember.CastMember;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;
import com.fullcycle.catalogo.admin.domain.category.CategoryID;
import com.fullcycle.catalogo.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record CastMemberListOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt
) {
    public static CastMemberListOutput from(final CastMember aMember) {
        return new CastMemberListOutput(
            aMember.getId().getValue(),
            aMember.getName(),
            aMember.getType(),
            aMember.getCreatedAt()
        );
    }
}
