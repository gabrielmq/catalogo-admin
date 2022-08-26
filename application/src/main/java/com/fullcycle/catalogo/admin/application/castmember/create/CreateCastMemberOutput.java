package com.fullcycle.catalogo.admin.application.castmember.create;

import com.fullcycle.catalogo.admin.domain.castmember.CastMember;

public record CreateCastMemberOutput(String id) {
    public static CreateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.getId().getValue());
    }

    public static CreateCastMemberOutput from(final String anId) {
        return new CreateCastMemberOutput(anId);
    }
}
