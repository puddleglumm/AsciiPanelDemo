package com.caled;

import asciiPanel.AsciiFont;
import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;


public class SnakeScreen extends BasicScreen {
    private int snakeDirection;
    int speed = 2;
    private Deque<Point> snakePoints;
    private final int height = 20;
    private final int width = 20;
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
            String finishScreenTitle = String.format("You finished! Score: %s", snakePoints.size() - 3);
            Screens.goToGameFinishScreen(application, finishScreenTitle);
        }
        renderBoard();

        terminal.repaint();
    }

    private boolean updateSnakePosition() {

        Point next = new Point(snakePoints.getLast());
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
            snakePoints.offer(next);
            if (applePos.equals(next)) {
                replaceApple();
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
        snakeDirection = 1;
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
            @Override
            public SnakeDirection turnLeft() {
                return Left;
            }
            @Override
            public SnakeDirection turnRight() {
                return Right;
            }
            @Override
            public SnakeDirection turnUp() {
                return Up;
            }
            @Override
            public SnakeDirection turnDown() {
                return Up;
            }
            @Override
            public Point delta() {
                return new Point(0, -1);
            }
        },
        Down {
            @Override
            public SnakeDirection turnLeft() {
                return Left;
            }
            @Override
            public SnakeDirection turnRight() {
                return Right;
            }
            @Override
            public SnakeDirection turnUp() {
                return Down;
            }
            @Override
            public SnakeDirection turnDown() {
                return Down;
            }
            @Override
            public Point delta() {
                return new Point(0, 1);
            }
        },
        Left {
            @Override
            public SnakeDirection turnLeft() {
                return Left;
            }
            @Override
            public SnakeDirection turnRight() {
                return Left;
            }
            @Override
            public SnakeDirection turnUp() {
                return Up;
            }
            @Override
            public SnakeDirection turnDown() {
                return Down;
            }
            @Override
            public Point delta() {
                return new Point(-1, 0);
            }
        },
        Right {
            @Override
            public SnakeDirection turnLeft() {
                return Right;
            }
            @Override
            public SnakeDirection turnRight() {
                return Right;
            }
            @Override
            public SnakeDirection turnUp() {
                return Up;
            }
            @Override
            public SnakeDirection turnDown() {
                return Down;
            }
            @Override
            public Point delta() {
                return new Point(1, 0);
            }
        };

        public abstract SnakeDirection turnLeft();
        public abstract SnakeDirection turnRight();
        public abstract SnakeDirection turnUp();
        public abstract SnakeDirection turnDown();
        public abstract Point delta();
    }
}
