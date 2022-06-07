package com.caled;

public class textPickerSelection implements Selection {
    private String title;
    private String[] contents;
    int current;

    textPickerSelection(String title, String[] options, int start) {
        this.title = title;
        this.contents = options;
        this.current = start;
    }

    textPickerSelection(String title, String[] options) {
        this.title = title;
        this.contents = options;
        this.current = 0;
    }

    public void onInteract(ScreenedApplication app) {
        this.onRight();
    }

    public void onLeft() {
        current--;
        if (current < 0) {
            current = contents.length - 1;
        }
    }

    public void onRight() {
        current++;
        if (current >= contents.length) {
            current = 0;
        }
    }

    public String display() {
        return (title + ": " + contents[current]);
    }

    public String getTitle() {
        return title;
    }

    public String getCurrent() {
        return contents[current];
    }
}
