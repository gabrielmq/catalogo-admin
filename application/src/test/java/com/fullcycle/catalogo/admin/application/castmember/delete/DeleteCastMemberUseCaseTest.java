package com.fullcycle.catalogo.admin.application.castmember.delete;

import com.fullcycle.catalogo.admin.application.Fixture;
import com.fullcycle.catalogo.admin.application.UseCaseTest;
import com.fullcycle.catalogo.admin.application.castmember.update.UpdateCastMemberCommand;
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

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_thenShouldDeleteIt() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedId = aMember.getId();

        doNothing().when(castMemberGateway).deleteById(any());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_thenShouldBeOk() {
        final var expectedId = CastMemberID.from("123");
        doNothing().when(castMemberGateway).deleteById(any());
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_thenShouldReceiveException() {
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMember.type());

        final var expectedId = aMember.getId();

        doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(any());
        assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
        verify(castMemberGateway, times(1)).deleteById(eq(expectedId));
    }
}
