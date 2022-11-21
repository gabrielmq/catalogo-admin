package com.fullcycle.catalogo.admin.domain.castmember;

import com.fullcycle.catalogo.admin.domain.Identifier;
import com.fullcycle.catalogo.admin.domain.utils.IDUtils;

import java.util.Objects;

public class CastMemberID extends Identifier {
    private final String value;

    private CastMemberID(final String anId) {
        this.value = Objects.requireNonNull(anId);
    }

    public static CastMemberID unique() {
        return from(IDUtils.uuid());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CastMemberID that = (CastMemberID) o;
        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
