package org.yokekhei.examples.fcm.model;

import java.util.ArrayList;
import java.util.List;

public class SubscribeFcmTopicRequest {

    private Long userId;
    private String topic;
    private List<String> tokens = new ArrayList<>();

    public SubscribeFcmTopicRequest() {
    }

    public SubscribeFcmTopicRequest(Long userId, String topic) {
        this.userId = userId;
        this.topic = topic;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public void addToken(String token) {
        tokens.add(token);
    }

    @Override
    public String toString() {
        return "SubscribeFcmTopicRequest [userId=" + userId + ", topic=" + topic + ", tokens=" + tokens + "]";
    }

}
