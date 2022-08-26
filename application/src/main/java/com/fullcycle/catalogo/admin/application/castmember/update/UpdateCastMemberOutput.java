package com.fullcycle.catalogo.admin.application.castmember.update;

import com.fullcycle.catalogo.admin.domain.castmember.CastMember;

public record UpdateCastMemberOutput(String id) {
    public static UpdateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.getId().getValue());
    }

    public static UpdateCastMemberOutput from(final String anId) {
        return new UpdateCastMemberOutput(anId);
    }
}
