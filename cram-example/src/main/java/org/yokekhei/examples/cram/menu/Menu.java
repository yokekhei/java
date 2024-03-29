package org.yokekhei.examples.cram.menu;

import java.util.Scanner;

public abstract class Menu {
    private static final int INVALID_CHOICE = -1;

    private Scanner scanner;

    public Menu(Scanner scanner) {
        this.scanner = scanner;
    }

    public abstract void run();

    protected Scanner getScanner() {
        return scanner;
    }

    protected int getInput(int min, int max) {
        int choice = INVALID_CHOICE;

        while (choice < min || choice > max) {
            System.out.print(System.lineSeparator() + "Enter your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());

                if (choice < min || choice > max) {
                    System.err.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid choice.");
            }
        }

        return choice;
    }

    protected abstract void print();
}
