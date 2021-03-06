package com.caled;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
import com.sun.media.jfxmedia.logging.Logger;

import javax.swing.*;
import java.awt.*;
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
    private int mspt = 100;

    private Dimension windowSize = new Dimension(864, 576);
    public ArrayList<Screen> screens = new ArrayList<>();

    public boolean typing = false;
    public boolean updateFont = false;


    public ScreenedApplication() throws InterruptedException {
        super();
        init();
    }

    public void keyPressed(KeyEvent e) {
        keyPressedSinceLastTick.add(e);
    }

    public void keyReleased(KeyEvent e) { }

    public void keyTyped(KeyEvent e) { }

    public static void main(String[] args) throws InterruptedException {
        ScreenedApplication app = new ScreenedApplication();

        app.run();
    }
    public AsciiPanel getTerminal() {
        return this.terminal;
    }

    public Screen getScreen(int screen) {
        return screens.get(screen);
    }

    public Screen getCurrentScreen() {
        return screens.get(currentScreen);
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
        mspt = getCurrentScreen().getMsPerTick();
        updateFont = true;
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

    public int widthInChars() {
        return windowSize.width / terminal.getCharWidth();
    }


    public int heightInChars() {
        return windowSize.height / terminal.getCharHeight();
    }

    private void init() {
        terminal = new AsciiPanel(96, 48, AsciiFont.CP437_9x16);

        Screens.initializeScreens(this);
        setScreen(0);
        updateFont = false;

        setSize(windowSize);
        add(terminal);
        addKeyListener(this);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void run() throws InterruptedException {
        setVisible(true);

        long previousTimeStamp;
        while (true) {
            previousTimeStamp = System.currentTimeMillis();
            getCurrentScreen().tick(keyPressedSinceLastTick);
            keyPressedSinceLastTick.clear();

            if (updateFont) {
                updateFont = false;
                AsciiFont newFont = getCurrentScreen().getFont();
                terminal.setAsciiFont(newFont);
            }

            long t = System.currentTimeMillis() - previousTimeStamp;
            if (t > mspt) {
                System.out.printf("tick took: %s ms, expected max is: %s%n", t, mspt);
            } else {
                Thread.sleep(mspt - t);
            }
        }
    }
}
