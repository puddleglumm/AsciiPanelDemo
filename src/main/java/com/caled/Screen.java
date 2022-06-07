package com.caled;

import asciiPanel.AsciiFont;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public interface Screen {
    void tick(ArrayList<KeyEvent> inputs);
    void reset();
    ScreenedApplication application();
    default int getMsPerTick() {
        return 100;
    }
    default AsciiFont getFont() {
        return AsciiFont.CP437_9x16;
    }
}
