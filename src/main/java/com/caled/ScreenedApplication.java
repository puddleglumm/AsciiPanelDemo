package com.caled;

import asciiPanel.AsciiPanel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class ScreenedApplication extends JFrame implements KeyListener
{

    private static final long serialVersionUID = 1060623638149583738L;

    private AsciiPanel terminal;
    private ArrayList<KeyEvent> keyPressedSinceLastTick = new ArrayList<>();
    private int currentScreen = 0;
    private int lastScreen = 0;
    public ArrayList<Screen> screens = new ArrayList<>();

    public boolean typing = false;


    public ScreenedApplication() throws InterruptedException {
        super();
        terminal = new AsciiPanel(80, 24);
        add(terminal);
        pack();
        addKeyListener(this);
    }

    public void keyPressed(KeyEvent e) {
        keyPressedSinceLastTick.add(e);
    }

    public void keyReleased(KeyEvent e) { }

    public void keyTyped(KeyEvent e) { }

    public static void main(String[] args) throws InterruptedException {
        ScreenedApplication app = new ScreenedApplication();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);

        Screens.initializeScreens(app);

        long previousTimeStamp;
        while (true) {
            previousTimeStamp = System.currentTimeMillis();
            app.screens.get(app.currentScreen).tick(app, app.keyPressedSinceLastTick);
            app.keyPressedSinceLastTick.clear();
            Thread.sleep(100 - (System.currentTimeMillis() - previousTimeStamp));
        }
    }
    public AsciiPanel getTerminal() {
        return this.terminal;
    }

    public Screen getScreen(int screen) {
        return screens.get(screen);
    }

    public void setScreen(int screen) {
        if (screen == Screens.KILL) {
            System.exit(0);
        } else if (screen == Screens.LAST) {
            int temp = lastScreen;
            lastScreen = currentScreen;
            currentScreen = temp;
        } else {
            lastScreen = currentScreen;
            currentScreen = screen;
        }
    }

    public void resetScreen(int screen) {
        Screen resetScreen;
        if (screen == Screens.LAST) {
            resetScreen = screens.get(lastScreen);
            resetScreen.reset();
            screens.set(lastScreen, resetScreen);
        } else if (!(screen == Screens.KILL)) {
            resetScreen = screens.get(screen);
            resetScreen.reset();
            screens.set(screen, resetScreen);
        }
    }

}
