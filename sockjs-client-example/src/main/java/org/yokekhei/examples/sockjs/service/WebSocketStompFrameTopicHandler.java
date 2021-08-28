package org.yokekhei.examples.sockjs.service;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.yokekhei.examples.sockjs.dto.Greeting;

public class WebSocketStompFrameTopicHandler implements StompFrameHandler {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketStompFrameTopicHandler.class);

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return Greeting.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
        Greeting message = (Greeting) payload;

        logger.info("Received >> " + message);
    }

}