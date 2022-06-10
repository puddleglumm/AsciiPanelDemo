package com.caled;

public class ListPickerSelection implements Selection {
    private String title;
    private String[] contents;
    int current;
    private String propertyName;

    ListPickerSelection(String title, String[] options) {
        this(title, options, "");
    }

    ListPickerSelection(String title, String[] options, String propertyName) {
        this.title = title;
        this.contents = options;
        this.current = 0;
        this.propertyName = propertyName;
    }
    public void onInteract(ScreenedApplication app) {
        this.onRight();
    }

    public static String[] stringArrayFromIntRange(int min, int max) {
        String[] options = new String[max + 1];
        for (int i = min; i <= max; i++) {
            options[i] = String.valueOf(i);
        }

        return options;
    }

    public void onLeft() {
        current--;
        if (current < 0) {
            current = contents.length - 1;
        }
        updateProperty();
    }

    public void onRight() {
        current++;
        if (current >= contents.length) {
            current = 0;
        }
        updateProperty();
    }

    private void updateProperty() {
        if (this.propertyName.equals("")) return;
        System.setProperty(this.propertyName, getCurrent());
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
