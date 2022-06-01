package com.caled;

public class TextPickerSelection implements Selection {
    String title;
    String[] contents;
    int current;

    TextPickerSelection(String title, String[] options, int start) {
        this.title = title;
        this.contents = options;
        this.current = start;
    }

    TextPickerSelection(String title, String[] options) {
        this.title = title;
        this.contents = options;
        this.current = 0;
    }

    public void onInteract(ScreenedApplication app) {
        this.onLeft();
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
}
