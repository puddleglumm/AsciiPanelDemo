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
    private ArrayList<Screen> screens = new ArrayList<>();
    private boolean run = true;


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

        long previousTimeStamp;
        app.screens.add(new SelectionScreen(new String[]{"[   play   ]","[ settings ]","[   quit   ]"}, "Connect Four", app, 2));
        app.screens.add(new ConnectFourScreen());
        app.screens.add(new SelectionScreen(new String[]{"[   play again    ]","[ return to title ]"}, "Red wins!", app, 2));
        app.screens.add(new SelectionScreen(new String[]{"[    continue     ]","[     restart     ]","[ return to title ]"}, "Paused", app, 2));
        System.out.println(app.currentScreen);
        while (app.run) {
            previousTimeStamp = System.currentTimeMillis();
            app.screens.get(app.currentScreen).tick(app, app.keyPressedSinceLastTick);
            app.keyPressedSinceLastTick.clear();
            Thread.sleep(100 - (System.currentTimeMillis() - previousTimeStamp));
        }
        System.exit(0);
    }
    public AsciiPanel getTerminal() {
        return this.terminal;
    }

    public Screen getScreen(int screen) {
        return screens.get(screen);
    }

    public void setScreen(int screen) {
        currentScreen = screen;
    }

    public void resetScreen(int screen) {
        Screen resetScreen = screens.get(screen);
        resetScreen.reset();
        screens.set(screen, resetScreen);
    }

    public void exit() {
        run = false;
    }
}
