package org.yokekhei.examples.cram.menu;

import org.apache.commons.lang3.StringUtils;

public class WelcomeScreen {

    private static final int SCREEN_WIDTH = 80;

    private String applicationName;

    public WelcomeScreen(String applicationName) {
        this.applicationName = applicationName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(getTopBottomLine());
        sb.append(getEmptyLine());

        if (applicationName.isEmpty()) {
            sb.append(getOutput("our Application"));
        } else {
            sb.append(getOutput(applicationName));
        }

        sb.append(getEmptyLine());
        sb.append(getTopBottomLine());

        return sb.toString();
    }

    private String getEmptyLine() {
        return StringUtils.center(StringUtils.center(" ", SCREEN_WIDTH - 2), SCREEN_WIDTH, "|")
                + System.lineSeparator();
    }

    private String getTopBottomLine() {
        return StringUtils.rightPad("+", SCREEN_WIDTH - 1, "-") + "+" + System.lineSeparator();
    }

    private String getOutput(String output) {
        return StringUtils.center(StringUtils.center(output, SCREEN_WIDTH - 2), SCREEN_WIDTH, "|")
                + System.lineSeparator();
    }

}
