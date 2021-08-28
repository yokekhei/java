package org.yokekhei.examples.fcm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yokekhei.examples.fcm.model.PushNotificationRequest;
import org.yokekhei.examples.fcm.model.PushNotificationResponse;
import org.yokekhei.examples.fcm.model.SubscribeFcmTopicRequest;
import org.yokekhei.examples.fcm.model.UnsubscribeFcmTopicRequest;
import org.yokekhei.examples.fcm.service.PushNotificationService;

@RestController
public class PushNotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;

    @PostMapping("/notification/subcribe")
    public String subscribeToTopic(@RequestBody SubscribeFcmTopicRequest request) {
        pushNotificationService.subscribeToTopic(request);
        return "Success";
    }

    @PostMapping("/notification/unsubcribe")
    public String unsubscribeFromTopic(@RequestBody UnsubscribeFcmTopicRequest request) {
        pushNotificationService.unsubscribeFromTopic(request);
        return "Success";
    }

    @PostMapping("/notification/topic")
    public ResponseEntity<PushNotificationResponse> sendTopicNotification(
            @RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToTopicWithoutData(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
                HttpStatus.OK);
    }

    @PostMapping("/notification/token")
    public ResponseEntity<PushNotificationResponse> sendTokenNotification(
            @RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToTokenWithoutData(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
                HttpStatus.OK);
    }

    @PostMapping("/notification/topic/data")
    public ResponseEntity<PushNotificationResponse> sendTopicDataNotification(
            @RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToTopicWithData(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
                HttpStatus.OK);
    }

    @PostMapping("/notification/token/data")
    public ResponseEntity<PushNotificationResponse> sendTokenDataNotification(
            @RequestBody PushNotificationRequest request) {
        pushNotificationService.sendPushNotificationToTokenWithData(request);
        return new ResponseEntity<>(new PushNotificationResponse(HttpStatus.OK.value(), "Notification has been sent."),
                HttpStatus.OK);
    }

}
