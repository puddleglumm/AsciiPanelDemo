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
        app.setScreen(redirect);
    }

    public void onLeft() {

    }

    public void onRight() {

    }

    public String display() {
        return title;
    }

    public void setRedirect(int redirect) {
        this.redirect = redirect;
    }

    public void setResets(int[] resets) {
        this.resets = resets;
    }

    public void setReset(int reset) {
        this.resets = new int[]{ reset };
    }
}
