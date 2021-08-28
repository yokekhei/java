package org.yokekhei.examples.fcm.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.yokekhei.examples.fcm.model.PushNotificationRequest;
import org.yokekhei.examples.fcm.model.SubscribeFcmTopicRequest;
import org.yokekhei.examples.fcm.model.UnsubscribeFcmTopicRequest;

import com.google.firebase.messaging.FirebaseMessagingException;

@Service
public class PushNotificationService {

    private static final Logger logger = LoggerFactory.getLogger(PushNotificationService.class);

    @Autowired
    private FCMService fcmService;
    @Value("#{${app.notifications.defaults}}")
    private Map<String, String> defaults;

    public void subscribeToTopic(SubscribeFcmTopicRequest request) {
        try {
            fcmService.subscribeToTopic(request);
        } catch (FirebaseMessagingException e) {
            logger.error(e.getMessage());
        }
    }

    public void unsubscribeFromTopic(UnsubscribeFcmTopicRequest request) {
        try {
            fcmService.unsubscribeFromTopic(request);
        } catch (FirebaseMessagingException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationToTopicWithData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToTopicWithData(getSamplePayloadData(), request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationToTopicWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToTopicWithoutData(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationToTokenWithoutData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToTokenWithoutData(request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void sendPushNotificationToTokenWithData(PushNotificationRequest request) {
        try {
            fcmService.sendMessageToTokenWithData(getSamplePayloadData(), request);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private Map<String, String> getSamplePayloadData() {
        Map<String, String> pushData = new HashMap<>();
        pushData.put("id", defaults.get("id"));
        pushData.put("caption", defaults.get("data") + " " + LocalDateTime.now());
        return pushData;
    }

}

