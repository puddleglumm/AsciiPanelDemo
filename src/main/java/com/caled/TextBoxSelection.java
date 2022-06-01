package com.caled;

public class TextBoxSelection implements Selection {
    private String title;
    public String text = "";
    private String hintText = "type here";

    TextBoxSelection(String title, String hintText) {
        this.title = title;
        this.hintText = hintText;
    }
    public void onInteract(ScreenedApplication app) {
        app.typing = true;
    }

    public void onLeft() {

    }

    public void onRight() {

    }

    public String display() {
        if (text.length() > 0) {
            return (title + ": " + text);
        } else { return (title + ": " + hintText); }
    }
}
