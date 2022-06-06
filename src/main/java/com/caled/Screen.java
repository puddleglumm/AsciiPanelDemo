package com.caled;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public interface Screen {
    void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs);
    void reset();
    default int getMsPerTick() {
        return 100;
    }
    default AsciiFont getFont() {
        return AsciiFont.CP437_9x16;
    }
}
