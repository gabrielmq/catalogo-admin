package com.fullcycle.catalogo.admin.infrastructure.amqp;

import com.fullcycle.catalogo.admin.application.video.media.update.UpdateMediaStatusCommand;
import com.fullcycle.catalogo.admin.application.video.media.update.UpdateMediaStatusUseCase;
import com.fullcycle.catalogo.admin.domain.video.media.MediaStatus;
import com.fullcycle.catalogo.admin.infrastructure.configuration.json.Json;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoEncoderCompleted;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoEncoderError;
import com.fullcycle.catalogo.admin.infrastructure.video.models.VideoEncoderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class VideoEncoderListener {
    private static final Logger LOG = LoggerFactory.getLogger(VideoEncoderListener.class);
    public static final String LISTENER_ID = "videoEncodedListener";

    private final UpdateMediaStatusUseCase useCase;

    public VideoEncoderListener(final UpdateMediaStatusUseCase useCase) {
        this.useCase = Objects.requireNonNull(useCase);
    }

    @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
    public void onVideoEncodedMessage(@Payload final String message) {
        final var aResult = Json.readValue(message, VideoEncoderResult.class);
        if (aResult instanceof VideoEncoderCompleted encoderCompleted) {
            LOG.info("[message:video.listener.income] [status:completed] [payload:{}]", message);
            final var aCommand = UpdateMediaStatusCommand.with(
                MediaStatus.COMPLETED,
                encoderCompleted.id(),
                encoderCompleted.video().resourceId(),
                encoderCompleted.video().encodedVideoFolder(),
                encoderCompleted.video().filepath()
            );
            useCase.execute(aCommand);
        } else if (aResult instanceof VideoEncoderError) {
            LOG.error("[message:video.listener.income] [status:error] [payload:{}]", message);
        } else {
            LOG.error("[message:video.listener.income] [status:unknown] [payload:{}]", message);
        }
    }
}
