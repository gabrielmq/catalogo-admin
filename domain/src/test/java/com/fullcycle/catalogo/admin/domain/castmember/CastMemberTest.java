package com.fullcycle.catalogo.admin.domain.castmember;

import com.fullcycle.catalogo.admin.domain.UnitTest;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember(expectedName, expectedType);
        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());
        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertNotNull(actualMember.getCreatedAt());
        assertNotNull(actualMember.getUpdatedAt());
    }

    @Test
    public void givenAInvalidNullName_whenCallsNewMember_thenShouldReceiveANotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualException =
            assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAInvalidEmptyName_whenCallsNewMember_thenShouldReceiveANotification() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualException =
            assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAInvalidNameWithLengthMoreThan255_whenCallsNewMember_thenShouldReceiveANotification() {
        final var expectedName = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualException =
            assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAInvalidNullType_whenCallsNewMember_thenShouldReceiveANotification() {
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualException =
            assertThrows(NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdate_thenShouldReceiveUpdated() {
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);
        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualCreatedAt = actualMember.getCreatedAt();
        final var actualUpdatedAt = actualMember.getUpdatedAt();

        actualMember.update(expectedName, expectedType);

        assertEquals(expectedName, actualMember.getName());
        assertEquals(expectedType, actualMember.getType());
        assertEquals(actualCreatedAt, actualMember.getCreatedAt());
        assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidNullName_thenShouldReceiveNotification() {
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);
        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException =
            assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidEmptyName_thenShouldReceiveNotification() {
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);
        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException =
                assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateWithNameLengthMoreThan255_thenShouldReceiveNotification() {
        final var expectedName = """
            O cuidado em identificar pontos críticos na consolidação das estruturas faz parte de um processo de gerenciamento dos índices pretendidos.
            Do mesmo modo, a complexidade dos estudos efetuados garante a contribuição de um grupo importante na determinação do fluxo de informações.
        """;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);
        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException =
                assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }

    @Test
    public void givenAValidCastMember_whenCallsUpdateWithInvalidNullType_thenShouldReceiveNotification() {
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);
        assertNotNull(actualMember);
        assertNotNull(actualMember.getId());

        final var actualException =
                assertThrows(NotificationException.class, () -> actualMember.update(expectedName, expectedType));

        assertNotNull(actualException);
        assertNotNull(expectedErrorMessage, actualException.getErrors().get(0).message());
        assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}
