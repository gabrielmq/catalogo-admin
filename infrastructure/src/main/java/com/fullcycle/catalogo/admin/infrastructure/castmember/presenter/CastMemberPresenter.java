package com.fullcycle.catalogo.admin.infrastructure.castmember.presenter;

import com.fullcycle.catalogo.admin.application.castmember.retrive.get.CastMemberOutput;
import com.fullcycle.catalogo.admin.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {
    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
            aMember.id(),
            aMember.name(),
            aMember.type(),
            aMember.createdAt(),
            aMember.updatedAt()
        );
    }
}
