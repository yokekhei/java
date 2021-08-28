package org.yokekhei.examples.sockjs.service;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

public class WebSocketStompFrameQueueErrorHandler implements StompFrameHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketStompFrameQueueErrorHandler.class);

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        String message = (String) payload;

        logger.info("Server exception >> " + message);
    }

}