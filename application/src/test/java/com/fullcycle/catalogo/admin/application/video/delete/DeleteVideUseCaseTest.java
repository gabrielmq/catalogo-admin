package com.fullcycle.catalogo.admin.application.video.delete;

import com.fullcycle.catalogo.admin.application.UseCaseTest;
import com.fullcycle.catalogo.admin.domain.exceptions.InternalErrorException;
import com.fullcycle.catalogo.admin.domain.video.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DeleteVideUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway resourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, resourceGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideo_thenShouldDeleteIt() {
        // given
        final var expectedId = VideoID.unique();

        doNothing().when(videoGateway).deleteById(any());
        doNothing().when(resourceGateway).clearResources(any());

        // when
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        verify(videoGateway).deleteById(eq(expectedId));
        verify(resourceGateway).clearResources(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteVideo_thenShouldBeOk() {
        // given
        final var expectedId = VideoID.from("123");

        doNothing().when(videoGateway).deleteById(any());

        // when
        assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //then
        verify(videoGateway).deleteById(eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_thenShouldReceiveException() {
        // given
        final var expectedId = VideoID.from("123");

        doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
            .when(videoGateway).deleteById(any());

        // when
        assertThrows(InternalErrorException.class, () -> useCase.execute(expectedId.getValue()));

        //then
        verify(videoGateway).deleteById(eq(expectedId));
    }
}
