package com.fullcycle.catalogo.admin.application.castmember.update;

import com.fullcycle.catalogo.admin.domain.Identifier;
import com.fullcycle.catalogo.admin.domain.castmember.CastMember;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand aCommand) {
        final var anId = CastMemberID.from(aCommand.id());

        final var aMember = castMemberGateway.findById(anId).orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.validate(() -> aMember.update(aCommand.name(), aCommand.type()));
        if (notification.hasErrors())  {
            throw new NotificationException(
                "Could not update Aggregate CastMember %s".formatted(anId.getValue()),
                notification
            );
        }
        return UpdateCastMemberOutput.from(castMemberGateway.update(aMember));
    }

    private Supplier<NotFoundException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }
}
