package com.caled;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Stack;

public class SnakeScreen implements Screen {

   int snakeDirection = 1;
    Stack<Point> snakePoints = new Stack<Point>(){{
        add(new Point(4, 0));
        add(new Point(3, 0));
        add(new Point(2, 0));
        add(new Point(1, 0));
        add(new Point(0, 0));
    }};

    @Override
    public AsciiFont getFont() {
        return AsciiFont.CP437_12x12;
    }

    @Override
    public void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();

        processInputs(application, inputs);

        if (!updateSnakePosition()) {
            SelectionScreen winScreen = (SelectionScreen) application.getScreen(Screens.GAME_FINISH);
            winScreen.setDisplayTitle(String.format("You finished! Score: %s", snakePoints.size()));
            application.setScreen(Screens.GAME_FINISH);
        }
        renderBoard(terminal);

        terminal.repaint();
    }

    private boolean updateSnakePosition() {

        Point next = new Point(snakePoints.get(0));
        if (Math.abs(snakeDirection) == 1) {
            next.x += snakeDirection;
        } else {
            next.y += (snakeDirection / 2);
        }

        boolean stillInBounds = (0 <= next.y && next.y < 24) && (0 <= next.x && next.x < 80);
        boolean collidedWithSelf = false;
        for (Point segment : snakePoints) {
            if (segment.x == next.x && segment.y == next.y) {
                collidedWithSelf = true;
                break;
            }
        }

        if (stillInBounds && !collidedWithSelf) {
            snakePoints.remove(snakePoints.size() - 1);
            snakePoints.insertElementAt(next, 0);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() {
        snakeDirection = 1;
        snakePoints = new Stack<Point>(){{
            add(new Point(4, 0));
            add(new Point(3, 0));
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

        boolean changedDir = false;
        for (KeyEvent input : inputs) {
            int keyCode = input.getKeyCode();

            if (keyCode == KeyEvent.VK_W && !changedDir && Math.abs(snakeDirection) == 1) {
                changedDir = true;
                snakeDirection = -2;
            } else if (keyCode == KeyEvent.VK_S && !changedDir && Math.abs(snakeDirection) == 1) {
                changedDir = true;
                snakeDirection = 2;
            } else if (keyCode == KeyEvent.VK_D && !changedDir && Math.abs(snakeDirection) == 2) {
                changedDir = true;
                snakeDirection = 1;
            } else if (keyCode == KeyEvent.VK_A && !changedDir && Math.abs(snakeDirection) == 2) {
                changedDir = true;
                snakeDirection = -1;
            } else if (input.getKeyCode() == KeyEvent.VK_ESCAPE) {
                application.setScreen(Screens.PAUSE);
            }
        }
    }
}
