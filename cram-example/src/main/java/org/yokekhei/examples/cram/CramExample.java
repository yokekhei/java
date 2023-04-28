package org.yokekhei.examples.cram;

import java.util.Scanner;

import org.yokekhei.examples.cram.menu.MainMenu;
import org.yokekhei.examples.cram.menu.Menu;
import org.yokekhei.examples.cram.menu.WelcomeScreen;

public class CramExample {

    public static void main(String[] args) {
        System.out.println(new WelcomeScreen("Challenge Response Authentication Mechanism Example"));
        Scanner sc = new Scanner(System.in);
        Menu menu = new MainMenu(sc);
        menu.run();
        sc.close();
    }

}
