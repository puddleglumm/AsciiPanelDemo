package com.caled;

import javax.swing.*;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class ApplicationMain extends JFrame implements KeyListener
{
    private static final long serialVersionUID = 1060623638149583738L;

    private AsciiPanel terminal;
    private ArrayList<KeyEvent> keyPressedSinceLastTick = new ArrayList<KeyEvent>();


    public ApplicationMain() throws InterruptedException {
        super();
        terminal = new AsciiPanel(80, 24);
        //terminal.write("starting screen", 40, 12);
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
        ApplicationMain app = new ApplicationMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        long previousTimeStamp;
        int i = 0;
        while (true) {
            previousTimeStamp = System.currentTimeMillis();
            i++;
            app.terminal.clear();
            app.terminal.write(String.valueOf(i), 0, 0, Color.yellow);
            app.terminal.repaint();
            app.keyPressedSinceLastTick.clear();
            Thread.sleep(100 - (System.currentTimeMillis() - previousTimeStamp));
        }
    }
}
