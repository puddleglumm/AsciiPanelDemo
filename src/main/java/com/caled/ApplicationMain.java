package com.caled;

import javax.swing.*;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;
/**
 * Hello world!
 *
 */
public class ApplicationMain extends JFrame
{
    private static final long serialVersionUID = 1060623638149583738L;

    private AsciiPanel terminal;

    public ApplicationMain() throws InterruptedException {
        super();
        terminal = new AsciiPanel(80, 24);
        terminal.write("* 1 2 3 4 5 6 7 8 # - ", 0, 20);
        //terminal.clear();
        //wait(1000);
        add(terminal);
        pack();
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationMain app = new ApplicationMain();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        app.setVisible(true);
        int x = 1;
        int y = 1;
        int x_dir = 1;
        int y_dir = 2;
        int timer = 0;
        while (true) {
            if (timer >= 500000000) {
                timer = 0;
                int terminal_height = app.terminal.getHeightInCharacters();
                int terminal_width = app.terminal.getWidthInCharacters();
                x += x_dir;
                y += y_dir;
                if (1 > x || x >= terminal_width - 1) { x_dir *= -1; x = Math.max(0, x); x = Math.min(x, terminal_width - 1); }
                if (1 > y || y >= terminal_height - 1) { y_dir *= -1; y = Math.max(0, y); y = Math.min(y, terminal_height - 1); }
                app.terminal.clear();
                app.terminal.write("#", x, y);
                app.terminal.updateUI();
            } else { timer++; }
        }
    }
}
