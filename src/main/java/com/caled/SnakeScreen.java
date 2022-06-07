package com.caled;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class SnakeScreen extends BasicScreen {
   private int snakeDirection;
   int speed = 2;
    private Stack<Point> snakePoints;

    private int height = 20;
    private int width = 20;
    private Point applePos;

    SnakeScreen(ScreenedApplication app) {
        super(app);
        reset();
    }

    @Override
    public AsciiFont getFont() {
        return AsciiFont.CP437_12x12;
    }

    @Override
    public int getMsPerTick() {
        return 200 / speed;
    }

    @Override
    public void tick(ArrayList<KeyEvent> inputs) {
        ScreenedApplication application = application();
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();

        processInputs(inputs);

        if (!updateSnakePosition()) {
            String finishScreenTitle = String.format("You finished! Score: %s", snakePoints.size());
            Screens.goToGameFinishScreen(application, finishScreenTitle);
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

        boolean stillInBounds = (0 <= next.y && next.y < height) && (0 <= next.x && next.x < width);
        boolean collidedWithSelf = false;
        for (Point segment : snakePoints) {
            if (segment.x == next.x && segment.y == next.y) {
                collidedWithSelf = true;
                break;
            }
        }

        if (stillInBounds && !collidedWithSelf) {
            snakePoints.insertElementAt(next, 0);
            if (applePos.equals(next)) {
                replaceApple();
            } else {
                snakePoints.remove(snakePoints.size() - 1);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() {
        snakeDirection = 1;
        snakePoints = new Stack<Point>(){{
            add(new Point(2 + (width/4), height/2));
            add(new Point(1 + (width/4), height/2));
            add(new Point(width/4, height/2));
        }};

        applePos = new Point(3 * (width/4), height/2);
    }

    private void renderBoard(AsciiPanel terminal) {
        int offsetX = (application().widthInChars() - width) / 2;
        int offsetY = (application().heightInChars() - height) / 2;

        for (int i = 0; i < width + 2; i++) {
            terminal.write("#", i + offsetX - 1, offsetY - 1);
            terminal.write("#", i + offsetX - 1, offsetY + height);
        }
        for (int i = 0; i < height; i++) {
            terminal.write("#", offsetX - 1, i + offsetY);
            terminal.write("#", offsetX + width, i + offsetY);
        }

        for (Point segment : snakePoints) {
            terminal.write("#", segment.x + offsetX, segment.y + offsetY, Color.GREEN);
        }

        terminal.write("@", applePos.x + offsetX, applePos.y + offsetY, Color.RED);
    }

    private void processInputs(ArrayList<KeyEvent> inputs) {
        ScreenedApplication application = application();

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

    private void replaceApple() {
        Random appleRNG = new Random();

        //TODO: Don't use bad infinite rng
        boolean inSnake = false;
        Point newPos = new Point(appleRNG.nextInt(width), appleRNG.nextInt(height));
        for (Point pos : snakePoints) {
            if (pos.x == newPos.x && pos.y == newPos.y) {
                inSnake = true;
            }
        }
        while (inSnake) {
            inSnake = false;
            newPos = new Point(appleRNG.nextInt(width), appleRNG.nextInt(height));
            for (Point pos : snakePoints) {
                if (pos.x == newPos.x && pos.y == newPos.y) {
                    inSnake = true;
                }
            }
        }

        applePos = newPos;
    }
}
