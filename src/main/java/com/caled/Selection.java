package com.caled;

public interface Selection {
    void onInteract(ScreenedApplication app);
    void onLeft();
    void onRight();
    String display();
}
