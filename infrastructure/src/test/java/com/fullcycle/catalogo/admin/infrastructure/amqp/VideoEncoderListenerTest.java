package com.fullcycle.catalogo.admin.infrastructure.amqp;

import com.fullcycle.catalogo.admin.AMQPTest;
import com.fullcycle.catalogo.admin.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.catalogo.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.catalogo.admin.domain.utils.IDUtils;
import com.fullcycle.catalogo.admin.domain.video.media.MediaStatus;
import com.fullcycle.catalogo.admin.infrastructure.configuration.annontations.VideoEncodedQueue;
import com.fullcycle.catalogo.admin.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.admin.infrastructure.configuration.properties.amqp.QueueProperties;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoEncoderCompleted;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoEncoderError;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoMessage;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoMetadata;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@AMQPTest
public class VideoEncoderListenerTest {
    @Autowired
    private TestRabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitListenerTestHarness harness;

    @MockBean
    private UpdateMediaStatusUseCase useCase;

    @Autowired
    @VideoEncodedQueue
    private QueueProperties props;

    @Test
    public void givenErrorResult_whenCallsListener_thenShouldProcess() throws InterruptedException {
        // given
        final var expectedError =
            new VideoEncoderError(new VideoMessage("123", "abc"), "Video not found");

        final var expectedMessage = Json.writeValueAsString(expectedError);

        // when
        this.rabbitTemplate.convertAndSend(props.getQueue(), expectedMessage);

        // then
        final var invocationData =
                harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void givenCompletedResult_whenCallsListener_thenShouldCallUseCase() throws InterruptedException {
        // given
        final var expectedId = IDUtils.uuid();
        final var expectedOutputBucket = "test";
        final var expectedStatus = MediaStatus.COMPLETED;
        final var expectedEncoderVideoFolder = "anyfolder";
        final var expectedResourceId = IDUtils.uuid();
        final var expectedFilePath = "any.mp4";
        final var expectedMetadata =
            new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

        final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

        final var expectedMessage = Json.writeValueAsString(aResult);

        doNothing().when(useCase).execute(any());

        // when
        this.rabbitTemplate.convertAndSend(props.getQueue(), expectedMessage);

        // then
        final var invocationData =
            harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

        assertNotNull(invocationData);
        assertNotNull(invocationData.getArguments());

        final var actualMessage = (String) invocationData.getArguments()[0];
        assertEquals(expectedMessage, actualMessage);

        final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);
        verify(useCase).execute(cmdCaptor.capture());

        final var actualCommand = cmdCaptor.getValue();
        assertEquals(expectedStatus, actualCommand.status());
        assertEquals(expectedId, actualCommand.videoId());
        assertEquals(expectedResourceId, actualCommand.resourceId());
        assertEquals(expectedEncoderVideoFolder, actualCommand.folder());
        assertEquals(expectedFilePath, actualCommand.filename());
    }
}
