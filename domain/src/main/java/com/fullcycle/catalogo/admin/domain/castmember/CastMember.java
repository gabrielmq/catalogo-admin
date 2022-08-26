package com.fullcycle.catalogo.admin.domain.castmember;

import com.fullcycle.catalogo.admin.domain.AggregateRoot;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import com.fullcycle.catalogo.admin.domain.utils.InstantUtils;
import com.fullcycle.catalogo.admin.domain.validation.ValidationHandler;
import com.fullcycle.catalogo.admin.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {
    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    private CastMember(
        final CastMemberID anId,
        final String aName,
        final CastMemberType aType,
        final Instant aCreationDate,
        final Instant aUpdateDate
    ) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var id = CastMemberID.unique();
        final var now = InstantUtils.now();
        return new CastMember(id, aName, aType, now, now);
    }

    public static CastMember with(
        final CastMemberID anId,
        final String aName,
        final CastMemberType aType,
        final Instant aCreationDate,
        final Instant aUpdateDate
    ) {
        return new CastMember(anId, aName, aType, aCreationDate, aUpdateDate);
    }

    public static CastMember with(final CastMember aMember) {
        return with(
            aMember.id,
            aMember.name,
            aMember.type,
            aMember.createdAt,
            aMember.updatedAt
        );
    }

    @Override
    public void validate(ValidationHandler aHandler) {
        new CastMemberValidator(this, aHandler).validate();
    }

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
        }
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
