package com.fullcycle.catalogo.admin.application.castmember.update;

import com.fullcycle.catalogo.admin.application.Fixture;
import com.fullcycle.catalogo.admin.application.UseCaseTest;
import com.fullcycle.catalogo.admin.domain.castmember.CastMember;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberGateway;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberID;
import com.fullcycle.catalogo.admin.domain.castmember.CastMemberType;
import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class UpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCastMember_thenShouldReturnItsIdentifier() {
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(CastMember.with(aMember)));
        when(castMemberGateway.update(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(expectedId.getValue(), actualOutput.id());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway)
            .update(argThat(aUpdatedMember ->
                Objects.equals(expectedId, aUpdatedMember.getId())
                && Objects.equals(expectedName, aUpdatedMember.getName())
                && Objects.equals(expectedType, aUpdatedMember.getType())
                && Objects.equals(aMember.getCreatedAt(), aUpdatedMember.getCreatedAt())
                && aMember.getUpdatedAt().isBefore(aUpdatedMember.getUpdatedAt())
            ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCastMember_thenShouldThrowsNotificationException() {
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(aMember));

        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidType_whenCallsUpdateCastMember_thenShouldThrowsNotificationException() {
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final var expectedName = Fixture.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.of(aMember));

        final var actualException =
            assertThrows(NotificationException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorCount, actualException.getErrors().size());
        assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    public void givenAInvalidID_whenCallsUpdateCastMember_thenShouldThrowsNotFoundException() {
        final var aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = CastMemberID.from("123");
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMembers.type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

        when(castMemberGateway.findById(any())).thenReturn(Optional.empty());

        final var actualException =
            assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        assertNotNull(actualException);
        assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }
}
