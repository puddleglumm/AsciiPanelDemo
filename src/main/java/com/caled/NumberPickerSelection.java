package com.caled;

public class NumberPickerSelection implements Selection {
    String title;
    int min;
    int max;
    int current;

    NumberPickerSelection(String title, int min, int max, int start) {
        this.title = title;
        this.min = min;
        this.max = max;
        this.current = start;
    }

    NumberPickerSelection(String title, int min, int max) {
        this.title = title;
        this.min = min;
        this.max = max;
        this.current = min;
    }

    public void onInteract(ScreenedApplication app) {
        this.onLeft();
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
}
