package org.yokekhei.examples.fcm.model;

public class PushNotificationRequest {

    private String title;
    private String message;
    private String topic;
    private String token;
    private String tag;

    public PushNotificationRequest() {
    }

    public PushNotificationRequest(String title, String message, String topic, String tag) {
        this.title = title;
        this.message = message;
        this.topic = topic;
        this.tag = tag;
    }

    public PushNotificationRequest(String title, String message, String topic, String token, String tag) {
        this.title = title;
        this.message = message;
        this.topic = topic;
        this.token = token;
        this.tag = tag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "PushNotificationRequest [title=" + title + ", message=" + message + ", topic=" + topic + ", token="
                + token + ", tag=" + tag + "]";
    }

}
