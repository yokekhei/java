package org.yokekhei.examples.qrCodeGenerator;

import java.io.IOException;
import java.util.Base64;
import java.util.Scanner;

import org.yokekhei.examples.qrCodeGenerator.util.QRCodeGenerator;

import com.google.zxing.WriterException;

public class MainMenu extends Menu {

    private boolean exit;

    private enum MenuOption {
        EXIT, GENERATE_QR_CODE_IMG, GENERATE_QR_CODE_BASE64
    };

    public MainMenu(Scanner scanner) {
        super(scanner);
    }

    @Override
    public void run() {
        while (!exit) {
            print();

            int choice = getInput(MenuOption.EXIT.ordinal(), MenuOption.GENERATE_QR_CODE_BASE64.ordinal());
            performAction(MenuOption.values()[choice]);
        }
    }

    @Override
    protected void print() {
        System.out.println(System.lineSeparator() + "Please make a selection: ");
        System.out.println("1) Generate QR code image");
        System.out.println("2) Generate QR code base64 string");
        System.out.println("0) Exit");
    }

    private void performAction(MenuOption choice) {
        switch (choice) {
        case EXIT:
            exit = true;
            System.out.println("Thank you for using our application.");
            break;

        case GENERATE_QR_CODE_IMG:
            generateQRCodeImage();
            break;

        case GENERATE_QR_CODE_BASE64:
            generateQRCodeBase64();
            break;

        default:
            System.err.println("An unknown error has occured.");
        }
    }

    private void generateQRCodeImage() {
        System.out.print(System.lineSeparator() + "Enter URL: ");
        String url = getScanner().nextLine();
        System.out.print("Enter file path : ");
        String filePath = getScanner().nextLine();

        try {
            QRCodeGenerator.generateQRCodeImage(url, 250, 250, filePath);
        } catch (WriterException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void generateQRCodeBase64() {
        System.out.print(System.lineSeparator() + "Enter URL: ");
        String url = getScanner().nextLine();

        try {
            byte[] imgBytes = QRCodeGenerator.getQRCodeImage(url, 250, 250);
            String encodedString = Base64.getEncoder().encodeToString(imgBytes);
            System.out.println(encodedString);
        } catch (WriterException | IOException e) {
            System.err.println(e.getMessage());
        }
    }

}
