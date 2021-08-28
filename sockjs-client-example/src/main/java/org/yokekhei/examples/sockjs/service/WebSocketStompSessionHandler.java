package org.yokekhei.examples.sockjs.service;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.messaging.simp.stomp.DefaultStompSession;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.messaging.tcp.TcpConnection;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.socket.sockjs.client.WebSocketClientSockJsSession;

public class WebSocketStompSessionHandler extends StompSessionHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketStompSessionHandler.class);

    private WebSocketStompServiceObserver observer;

    public WebSocketStompSessionHandler(WebSocketStompServiceObserver observer) {
        this.observer = observer;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, @Nullable Object payload) {
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.debug("New session established : " + session.getSessionId());

        DefaultStompSession defaultStompSession = (DefaultStompSession) session;
        Field fieldConnection = ReflectionUtils.findField(DefaultStompSession.class, "connection");
        fieldConnection.setAccessible(true);
        String sockjsSessionId = "";

        try {
            TcpConnection<byte[]> connection = (TcpConnection<byte[]>) fieldConnection.get(defaultStompSession);

            try {
                Class adapter = Class.forName("org.springframework.web.socket.messaging.WebSocketStompClient$WebSocketTcpConnectionHandlerAdapter");
                Field fieldSession = ReflectionUtils.findField(adapter, "session");
                fieldSession.setAccessible(true);
                WebSocketClientSockJsSession sockjsSession = (WebSocketClientSockJsSession) fieldSession.get(connection);
                sockjsSessionId = sockjsSession.getId();
            } catch (ClassNotFoundException e) {
                logger.error(e.getMessage());
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }

        if (sockjsSessionId == null || sockjsSessionId.isEmpty()) {
            throw new IllegalStateException("couldn't extract sock.js session id");
        }

        observer.onConnected(sockjsSessionId);
    }

    @Override
    public void handleException(StompSession session, @Nullable StompCommand command, StompHeaders headers,
            byte[] payload, Throwable exception) {
        throw new RuntimeException("Failure in WebSocket handling", exception);
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        throw new RuntimeException("Failure in WebSocket transport handling", exception);
    }

}
