package com.fullcycle.catalogo.admin.application.castmember.create;

import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;

public record CreateCastMemberCommand(
    String name,
    CastMemberType type
) {
    public static CreateCastMemberCommand with(final String aName, final CastMemberType aType) {
        return new CreateCastMemberCommand(aName, aType);
    }
}
