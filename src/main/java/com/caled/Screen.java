package com.caled;

import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public interface Screen {
    void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs);
    void reset();
}
