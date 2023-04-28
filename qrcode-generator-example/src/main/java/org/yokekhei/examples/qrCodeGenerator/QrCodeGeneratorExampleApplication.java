package org.yokekhei.examples.qrCodeGenerator;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class QrCodeGeneratorExampleApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(QrCodeGeneratorExampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(new WelcomeScreen("QR Code Generator Example"));
        Scanner sc = new Scanner(System.in);
        Menu menu = new MainMenu(sc);
        menu.run();
        sc.close();
    }

}
