package com.caled;

import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Stack;

public class SnakeScreen implements Screen {

   int snakeDirection = 1;
    Stack<Point> snakePoints = new Stack<Point>(){{
        add(new Point(2, 0));
        add(new Point(1, 0));
        add(new Point(0, 0));
    }};

    @Override
    public void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();

        processInputs(application, inputs);
        updateSnakePosition();
        renderBoard(terminal);

        terminal.repaint();
    }

    private void updateSnakePosition() {
        snakePoints.remove(snakePoints.size() - 1);

        Point next = new Point(snakePoints.get(0));
        if (Math.abs(snakeDirection) == 1) {
            next.x += snakeDirection;
        } else {
            next.y += (snakeDirection / 2);
        }

        snakePoints.insertElementAt(next, 0);
    }

    @Override
    public void reset() {
        snakeDirection = 1;
        snakePoints = new Stack<Point>(){{
            add(new Point(2, 0));
            add(new Point(1, 0));
            add(new Point(0, 0));
        }};
    }

    private void renderBoard(AsciiPanel terminal) {
        for (Point segment : snakePoints) {
            terminal.write("#", segment.x, segment.y, Color.GREEN);
        }
    }

    private void processInputs(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        for (KeyEvent input : inputs) {
            int keyCode = input.getKeyCode();

            if (keyCode == KeyEvent.VK_W) {
                if (Math.abs(snakeDirection) == 1) {
                    snakeDirection = -2;
                }
            } else if (keyCode == KeyEvent.VK_S) {
                if (Math.abs(snakeDirection) == 1) {
                    snakeDirection = 2;
                }
            } else if (keyCode == KeyEvent.VK_D) {

                if (Math.abs(snakeDirection) == 2) {
                    snakeDirection = 1;
                }
            } else if (keyCode == KeyEvent.VK_A) {

                if (Math.abs(snakeDirection) == 2) {
                    snakeDirection = -1;
                }
            } else if (input.getKeyCode() == KeyEvent.VK_ESCAPE) {
                application.setScreen(Screens.PAUSE);
            }
        }
    }
}
