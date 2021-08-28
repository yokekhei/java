package org.yokekhei.examples.websocket.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import org.yokekhei.examples.websocket.dto.Comment;
import org.yokekhei.examples.websocket.dto.Greeting;
import org.yokekhei.examples.websocket.dto.HelloMessage;

@Controller
public class WebSocketController {

    @Value("${server.servlet.contextPath}")
    private String contextPath;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

//    @MessageMapping("/topic/hello")
//    @SendTo("/java-example/topic/greetings")
//    public Greeting greetingAll(HelloMessage message) throws Exception {
//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//    }

    @MessageMapping("/topic/hello")
    public void greetingAll(HelloMessage message) throws Exception {
        Greeting out = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
        simpMessagingTemplate.convertAndSend(contextPath + "/topic/greetings", out);
    }

    @MessageMapping("/topic/comments")
    public void addComment(Comment comment) {
        comment.setId(Long.valueOf((new Random().nextInt(10) + 1)));
        comment.setDateTime(new SimpleDateFormat("YYMMddHHmmss").format(new Date()));
        simpMessagingTemplate.convertAndSend(contextPath + "/topic/comments", comment);
    }

//    @MessageMapping("/queue/hello")
//    @SendToUser("/java-example/queue/greetings")
//    public Greeting greetingSingleUser(@Payload HelloMessage message, Principal principal) {
//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//    }

    @MessageMapping("/queue/hello")
    public void greetingSingleUser(@Payload HelloMessage message, Principal principal,
            @Header("simpSessionId") String sessionId) {
        Greeting out = new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
        simpMessagingTemplate.convertAndSendToUser(sessionId, contextPath + "/queue/greetings", out,
                createHeaders(sessionId));
    }

    @MessageExceptionHandler
    @SendToUser("/java-example/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }

    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

}
