package org.yokekhei.examples.sockjs.service;

public interface WebSocketStompService {

    void register(WebSocketStompServiceListener listener);

    void connect();

    void disconnect();

    void subscribe(String sessionId);

    void sendTopic(String message);

    void sendQueue(String message);

}
