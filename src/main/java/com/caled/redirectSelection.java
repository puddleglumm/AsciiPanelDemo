package com.caled;

public class redirectSelection implements Selection {
    String title;
    int redirect;
    int[] resets;

    redirectSelection(String title, int screen, int[] reset) {
        this.resets = reset;
        this.title = title;
        this.redirect = screen;
    }
    redirectSelection(String title, int screen, int reset) {
        this.resets = new int[]{ reset };
        this.title = title;
        this.redirect = screen;
    }
    redirectSelection(String title, int screen) {
        this.resets = new int[]{};
        this.title = title;
        this.redirect = screen;
    }

    public void onInteract(ScreenedApplication app) {
        for (int i : resets) {
            app.resetScreen(i);
        }
        if (redirect == Screens.KILL) {
            System.exit(0);
        } else {
            app.setScreen(redirect);
        }
    }

    public void onLeft() {

    }

    public void onRight() {

    }

    public String display() {
        return title;
    }
}
