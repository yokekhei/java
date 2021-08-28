package org.yokekhei.examples.sockjs;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yokekhei.examples.sockjs.service.WebSocketStompService;
import org.yokekhei.examples.sockjs.service.WebSocketStompServiceListener;

@SpringBootApplication
public class SockjsClientExampleApplication implements ApplicationRunner, WebSocketStompServiceListener {

    @Autowired
    private WebSocketStompService service;
    @Value("${websocket.message.number}")
    private Integer numMessages;
    private boolean isReadyToSend = false;

    @Override
    public void onSubscribed() {
        isReadyToSend = true;
    }

    public static void main(String[] args) {
        SpringApplication.run(SockjsClientExampleApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        service.register(this);
        service.connect();

        Runtime.getRuntime().addShutdownHook(new Thread("shutdown-hook") {
            @Override
            public void run() {
                service.disconnect();
                scanner.close();
            }
        });

        while (true) {
            if (!isReadyToSend) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                continue;
            }

            System.out.println("Enter destination choices (Topic-0, Queue-1: ");
            int choice = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter message : ");
            String message = scanner.nextLine();
            if (choice == 0)
                sendTopic(message);
            else
                sendQueue(message);
        }
    }

    private void sendTopic(String message) {
        for (int i = 0; i < numMessages; i++) {
            service.sendTopic(message);
        }
    }

    private void sendQueue(String message) {
        for (int i = 0; i < numMessages; i++) {
            service.sendQueue(message);
        }
    }

}
