package org.yokekhei.examples.sockjs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.yokekhei.examples.sockjs.dto.HelloMessage;

@Service
public class WebSocketStompServiceImpl implements WebSocketStompService, WebSocketStompServiceObserver {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketStompServiceImpl.class);

    private enum EVENT {
        SUBSCRIBED
    };

    @Value("${spring.application.name}")
    private String applicationName;
    @Value("${websocket.url}")
    private String url;
    @Value("${websocket.endpoint.app.topic}")
    private String appTopicEndpoint;
    @Value("${websocket.endpoint.app.queue}")
    private String appQueueEndpoint;
    @Value("${websocket.endpoint.topic}")
    private String topicEndpoint;
    @Value("${websocket.endpoint.queue}")
    private String queueEndpoint;
    @Value("${websocket.endpoint.queue.errors}")
    private String queueErrorsEndpoint;
    private SockJsClient sockJsClient;
    private WebSocketStompClient stompClient;
    private StompSession stompSession;
    private Subscription subscription;
    private List<WebSocketStompServiceListener> listeners = new ArrayList<>();

    public WebSocketStompServiceImpl() {
        sockJsClient = new SockJsClient(createTransportClient());
        stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Override
    public void register(WebSocketStompServiceListener listener) {
        listeners.add(listener);
    }

    @Override
    public void connect() {
        try {
            stompSession = stompClient.connect(url, new WebSocketStompSessionHandler(this)).get(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
            ;
        } catch (TimeoutException e) {
            logger.error(e.getMessage());
            ;
        }
    }

    @Override
    public void disconnect() {
        stompSession.disconnect();
    }

    @Override
    public void subscribe(String sessionId) {
        subscription = stompSession.subscribe(topicEndpoint, new WebSocketStompFrameTopicHandler());

        StompHeaders headers = subscription.getSubscriptionHeaders();
        logger.debug("Subscription header is " + headers.toString());
        logger.debug("Subscription id is " + subscription.getSubscriptionId());

        subscription = stompSession.subscribe(queueEndpoint, new WebSocketStompFrameQueueHandler());
        subscription = stompSession.subscribe(queueErrorsEndpoint, new WebSocketStompFrameQueueErrorHandler());

        notifyListeners(EVENT.SUBSCRIBED);
    }

    @Override
    public void sendTopic(String message) {
        HelloMessage helloMessage = new HelloMessage(message);
        stompSession.send(appTopicEndpoint, helloMessage);

        logger.info("Sent << " + helloMessage);
    }

    @Override
    public void sendQueue(String message) {
        HelloMessage helloMessage = new HelloMessage(message);
        stompSession.send(appQueueEndpoint, helloMessage);

        logger.info("Sent << " + helloMessage);
    }

    @Override
    public void onConnected(String sessionId) {
        logger.info("Connected to " + url + " with session id " + sessionId);

        subscribe(sessionId);
    }

    private List<Transport> createTransportClient() {
        final List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private void notifyListeners(EVENT event) {
        switch (event) {
        case SUBSCRIBED:
            listeners.forEach(l -> l.onSubscribed());
            break;
        default:
            logger.error("Unknown event " + event);
            break;
        }
    }

}
