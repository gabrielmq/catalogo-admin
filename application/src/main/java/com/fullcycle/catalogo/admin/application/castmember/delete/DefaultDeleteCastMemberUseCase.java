package com.fullcycle.catalogo.admin.application.castmember.delete;

import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;

import java.util.Objects;

public final class DefaultDeleteCastMemberUseCase extends DeleteCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultDeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(final String anIn) {
        castMemberGateway.deleteById(CastMemberID.from(anIn));
    }
}
