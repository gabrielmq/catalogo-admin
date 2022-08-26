package com.fullcycle.catalogo.admin.application.castmember.create;

import com.fullcycle.catalogo.admin.domain.castmember.CastMember;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;

import java.util.Objects;

public non-sealed class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final var notification = Notification.create();
        final var aMember =
            notification.validate(() -> CastMember.newMember(aCommand.name(), aCommand.type()));

        if (notification.hasErrors())  {
            throw new NotificationException("Could not create Aggregate CastMember", notification);
        }
        return CreateCastMemberOutput.from(castMemberGateway.create(aMember));
    }
}
