package com.caled;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class BasicScreen implements Screen {

    ScreenedApplication app;

    BasicScreen(ScreenedApplication app) {
        this.app = app;
    }

    @Override
    public void tick(ArrayList<KeyEvent> inputs) {

    }

    @Override
    public void reset() {

    }

    @Override
    public ScreenedApplication application() {
        return app;
    }
}
