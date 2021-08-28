package org.yokekhei.examples.websocket.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.yokekhei.examples.websocket.dto.Notification;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class NotificationController {

    @Value("${server.servlet.contextPath}")
    private String contextPath;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/notification")
    @ResponseBody
    public Notification createNotification(@Validated @RequestBody Notification notification) {
        notification.setId(Long.valueOf((new Random().nextInt(10) + 1)));
        notification.setDateTime(new SimpleDateFormat("YYMMddHHmmss").format(new Date()));

        simpMessagingTemplate.convertAndSend(contextPath + "/topic/notification", notification);

        return notification;
    }

}
