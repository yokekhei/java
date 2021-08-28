package org.yokekhei.examples.fcm.service;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yokekhei.examples.fcm.model.PushNotificationRequest;
import org.yokekhei.examples.fcm.model.SubscribeFcmTopicRequest;
import org.yokekhei.examples.fcm.model.UnsubscribeFcmTopicRequest;

import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.TopicManagementResponse;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class FCMService {

    private static final Logger logger = LoggerFactory.getLogger(FCMService.class);
    @Value("${app.notifications.icon.url}")
    private String iconUrl;

    public void subscribeToTopic(SubscribeFcmTopicRequest request) throws FirebaseMessagingException {
        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(request.getTokens(),
                request.getTopic());
        logger.info(response.getSuccessCount() + " tokens were subscribed successfully");
    }

    public void unsubscribeFromTopic(UnsubscribeFcmTopicRequest request) throws FirebaseMessagingException {
        TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(request.getTokens(),
                request.getTopic());
        logger.info(response.getSuccessCount() + " tokens were unsubscribed successfully");
    }

    public void sendMessageToTopicWithData(Map<String, String> data, PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToTopicWithData(data, request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        logger.info("Sent message with data. Topic: " + request.getTopic() + ", " + response + " msg " + jsonOutput);
    }

    public void sendMessageToTopicWithoutData(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToTopicWithoutData(request);
        String response = sendAndGetResponse(message);
        logger.info("Sent message without data. Topic: " + request.getTopic() + ", " + response);
    }

    public void sendMessageToTokenWithoutData(PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToTokenWithoutData(request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        logger.info(
                "Sent message to token without data. Device token: " + request.getToken() + ", " + response + " msg "
                        + jsonOutput);
    }

    public void sendMessageToTokenWithData(Map<String, String> data, PushNotificationRequest request)
            throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageToTokenWithData(data, request);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonOutput = gson.toJson(message);
        String response = sendAndGetResponse(message);
        logger.info("Sent message to token with data. Device token: " + request.getToken() + ", " + response + " msg "
                + jsonOutput);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private AndroidConfig getAndroidConfig(PushNotificationRequest request) {
        return AndroidConfig.builder().setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(request.getTopic())
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
                        .setColor(NotificationParameter.COLOR.getValue()).setTag(request.getTag())
                        .setBody(request.getMessage()).setTitle(request.getTitle()).setIcon(iconUrl).build())
                .build();
    }

    private ApnsConfig getApnsConfig(PushNotificationRequest request) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(request.getTag()).setThreadId(request.getTag()).build()).build();
    }

    private WebpushConfig getWebpushConfig(PushNotificationRequest request) {
        return WebpushConfig.builder()
                .setNotification(WebpushNotification.builder().setTitle(request.getTitle())
                        .setBody(request.getMessage()).setTag("OUT_OF_STOCK")
                        .setIcon(iconUrl).setTimestampMillis((new Date()).getTime()).build())
                .build();
    }

    private Message getPreconfiguredMessageToTokenWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
    }

    private Message getPreconfiguredMessageToTopicWithoutData(PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic()).build();
    }

    private Message getPreconfiguredMessageToTokenWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setToken(request.getToken()).build();
    }

    private Message getPreconfiguredMessageToTopicWithData(Map<String, String> data, PushNotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.getTopic()).build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(PushNotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request);
        ApnsConfig apnsConfig = getApnsConfig(request);
        WebpushConfig webpushConfig = getWebpushConfig(request);
        return Message.builder().setWebpushConfig(webpushConfig)
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
                .setNotification(
                        Notification.builder().setTitle(request.getTitle()).setBody(request.getMessage()).build());
    }

}

