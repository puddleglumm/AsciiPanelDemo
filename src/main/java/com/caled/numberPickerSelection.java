package com.caled;

public class numberPickerSelection implements Selection {
    private String title;
    private int min;
    private int max;
    private int current;

    numberPickerSelection(String title, int min, int max, int start) {
        this.title = title;
        this.min = min;
        this.max = max;
        this.current = start;
    }

    numberPickerSelection(String title, int min, int max) {
        this.title = title;
        this.min = min;
        this.max = max;
        this.current = min;
    }

    public void onInteract(ScreenedApplication app) {
        this.onRight();
    }

    public void onLeft() {
        current--;
        if (current < min) {
            current = max;
        }
    }

    public void onRight() {
        current++;
        if (current > max) {
            current = min;
        }
    }

    public String display() {
        return title + ": " + String.valueOf(current);
    }

    public int getCurrent() {
        return current;
    }

    public String getTitle() {
        return title;
    }
}
