package com.fullcycle.catalogo.admin.application.video.media.get;

import com.fullcycle.catalogo.admin.application.UseCaseTest;
import com.fullcycle.catalogo.admin.domain.Fixture;
import com.fullcycle.catalogo.admin.domain.exceptions.NotFoundException;
import com.fullcycle.catalogo.admin.domain.video.MediaResourceGateway;
import com.fullcycle.catalogo.admin.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GetMediaUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultGetMediaUseCase  useCase;

    @Mock
    private MediaResourceGateway gateway;


    @Override
    protected List<Object> getMocks() {
        return List.of(gateway);
    }

    @Test
    public void givenVideoIdAndType_whenIsValidCommand_thenShouldReturnResource() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(gateway.getResource(expectedId, expectedType)).thenReturn(Optional.of(expectedResource));

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        // when
        final var actualResult = useCase.execute(aCommand);

        // then
        assertEquals(expectedResource.name(), actualResult.name());
        assertEquals(expectedResource.contentType(), actualResult.contentType());
        assertEquals(expectedResource.content(), actualResult.content());
    }

    @Test
    public void givenVideoIdAndType_whenIsNotFound_thenShouldReturnNotFoundException() {
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();

        when(gateway.getResource(expectedId, expectedType)).thenReturn(Optional.empty());

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));
    }

    @Test
    public void givenVideoIdAndType_whenTypeDoesntExists_thenShouldReturnNotFoundException() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "MediaType TYPE doesn't exists";

        final var aCommand = GetMediaCommand.with(expectedId.getValue(), "TYPE");

        // when
        final var actualException =
            assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        // then
        assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
