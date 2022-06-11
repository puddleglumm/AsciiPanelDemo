package com.caled;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;


public class SnakeScreen extends BasicScreen {

    public final static String BOARD_HEIGHT_PROPERTY_NAME = SnakeScreen.class.getCanonicalName() + ".boardheight";
    public final static String BOARD_WIDTH_PROPERTY_NAME = SnakeScreen.class.getCanonicalName() + ".boardwidth";
    public final static String SNAKE_SPEED_PROPERTY_NAME = SnakeScreen.class.getCanonicalName() + ".snakespeed";
    private SnakeDirection snakeDirection;
    int speed = 2;
    private Deque<Point> snakePoints;
    private int height = 20;
    private int width = 20;
    private Point applePos;
    private HashSet<Point> appleCandidatePoints;
    private final Random rng = new Random();


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
            Sounds.playSound(Sounds.SNAKE_BOINK);
            String finishScreenTitle = String.format("You finished! Score: %s", snakePoints.size() - 3);
            Screens.goToGameFinishScreen(application, finishScreenTitle);
        }
        renderBoard();

        terminal.repaint();
    }

    private boolean updateSnakePosition() {

        Point next = new Point(snakePoints.getLast());
        next.translate(snakeDirection.delta().x, snakeDirection.delta().y);

        boolean stillInBounds = (0 <= next.y && next.y < height) && (0 <= next.x && next.x < width);
        boolean collidedWithSelf = false;
        for (Point segment : snakePoints) {
            if (segment.x == next.x && segment.y == next.y) {
                collidedWithSelf = true;
                break;
            }
        }

        if (stillInBounds && !collidedWithSelf) {
            snakePoints.offer(next);
            if (applePos.equals(next)) {
                replaceApple();
                Sounds.playSound(Sounds.SNAKE_MUNCH);
            } else {
                snakePoints.remove();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() {
        height = Integer.parseInt(System.getProperty(BOARD_HEIGHT_PROPERTY_NAME, "20"));
        width = Integer.parseInt(System.getProperty(BOARD_WIDTH_PROPERTY_NAME, "20"));
        speed = Integer.parseInt(System.getProperty(SNAKE_SPEED_PROPERTY_NAME, "2"));

        snakeDirection = SnakeDirection.Right;
        Point start = new Point(width / 4, height / 2);

        snakePoints = new ArrayDeque<>(Arrays.asList(
            new Point(start.x, start.y),
            new Point(1 + start.x, start.y),
            new Point(2 + start.x, start.y)
        ));

        applePos = new Point(3 * start.x, start.y);

        appleCandidatePoints = new HashSet<>(width * height);
        for (int i_x = 0; i_x < width; i_x++) {
            for (int i_y = 0; i_y < height; i_y++) {
                appleCandidatePoints.add(new Point(i_x, i_y));
            }
        }
    }

    private void renderBoard() {
        AsciiPanel terminal = application().getTerminal();

        int offsetX = (application().widthInChars() - width) / 2;
        int offsetY = (application().heightInChars() - height) / 2;

        terminal.write(String.format("Score: %s", snakePoints.size() - 3), offsetX + (width - 7)/2, offsetY - 3, Color.YELLOW);

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

            if (input.getKeyCode() == KeyEvent.VK_ESCAPE) {
                application.setScreen(Screens.PAUSE);
            } else if (changedDir) {
                continue;
            }

            switch (keyCode) {
                case KeyEvent.VK_W:
                    changedDir = tryChangingSnakeDirectionTo(SnakeDirection.Up);
                    break;
                case KeyEvent.VK_S:
                    changedDir = tryChangingSnakeDirectionTo(SnakeDirection.Down);
                    break;
                case KeyEvent.VK_D:
                    changedDir = tryChangingSnakeDirectionTo(SnakeDirection.Right);
                    break;
                case KeyEvent.VK_A:
                    changedDir = tryChangingSnakeDirectionTo(SnakeDirection.Left);
                    break;
            }
        }
    }

    private boolean tryChangingSnakeDirectionTo(SnakeDirection direction) {
        if (snakeDirection.canTurn(direction)) {
            snakeDirection = direction;
            return true;
        }
        return false;
    }

    private void replaceApple() {
        appleCandidatePoints.removeAll(snakePoints);

        int i = rng.nextInt(appleCandidatePoints.size());
        for (Point p : appleCandidatePoints) {
            i--;
            if (i == 0) {
                applePos.setLocation(p);
                break;
            }
        }

        appleCandidatePoints.addAll(snakePoints);
    }

    public enum SnakeDirection {
        Up {
            public boolean canTurn(SnakeDirection direction)  { return direction == Left || direction == Right; }
            public Point delta() { return new Point(0, -1); }
        },
        Down {
            public boolean canTurn(SnakeDirection direction)  { return direction == Left || direction == Right; }
            public Point delta() { return new Point(0, 1); }
        },
        Left {
            public boolean canTurn(SnakeDirection direction)  { return direction == Up || direction == Down; }
            public Point delta() { return new Point(-1, 0); }
        },
        Right {
            public boolean canTurn(SnakeDirection direction)  { return direction == Up || direction == Down; }
            public Point delta() { return new Point(1, 0); }
        };

        public abstract boolean canTurn(SnakeDirection direction);
        public abstract Point delta();
    }
}
