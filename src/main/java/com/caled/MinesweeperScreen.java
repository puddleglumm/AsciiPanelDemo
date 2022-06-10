package com.caled;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import asciiPanel.AsciiPanel;
import com.github.puddleglumm.minesweeper.*;

public class MinesweeperScreen extends BasicScreen {

    public final static String BOARD_HEIGHT_PROPERTY_NAME = MinesweeperScreen.class.getCanonicalName() + ".boardheight";
    public final static String BOARD_WIDTH_PROPERTY_NAME = MinesweeperScreen.class.getCanonicalName() + ".boardwidth";
    public final static String MINECOUNT_PROPERTY_NAME = MinesweeperScreen.class.getCanonicalName() + ".minecount";
    Board board = new Board(20, 20, 21);
    Point cursor = new Point(0, 0);
    private final String flagChar = "\u00E2";
    private final String unrevealedTileChar = "#";
    private final String emptyTileChar = ".";
    private final Color cursorBgColor = new Color(160, 130, 0);
    HashMap<String, Color> colorForTile = new HashMap<String, Color>() {{
        put(unrevealedTileChar, Color.LIGHT_GRAY);
        put(emptyTileChar, Color.DARK_GRAY);
        put("1", Color.BLUE);
        put("2", Color.GREEN);
        put("3", Color.RED);
        put("4", new Color(0, 0, 120));
        put("5", new Color(120, 0, 0));
        put("6", new Color(0, 110, 110));
        put("7", Color.BLACK);
        put("8", Color.DARK_GRAY);
        put(flagChar, Color.ORANGE);
    }};

    MinesweeperScreen(ScreenedApplication app) {
        super(app);
    }
    @Override
    public void tick(ArrayList<KeyEvent> inputs) {
        ScreenedApplication application = application();
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();

        if (!board.revealInProgress()) {
            processInputs(inputs);
        } else {
            board.continueRevealing();
        }

        if (board.checkWin()) {
            Screens.goToGameFinishScreen(application, "You win!");
        }

        renderBoard(application);
        terminal.repaint();
    }

    @Override
    public void reset() {
        int boardHeight = Integer.parseInt(System.getProperty(BOARD_HEIGHT_PROPERTY_NAME, "20"));
        int boardWidth = Integer.parseInt(System.getProperty(BOARD_WIDTH_PROPERTY_NAME, "20"));
        int mineCount = Integer.parseInt(System.getProperty(MINECOUNT_PROPERTY_NAME, "21"));
        if (mineCount >= boardWidth * boardHeight) mineCount = boardWidth * boardHeight - 1;
        board = new Board(boardHeight, boardWidth, mineCount);
        cursor.x = 0;
        cursor.y = 0;
    }

    private void renderBoard(ScreenedApplication app) {
        int offsetX = app.widthInChars()/2 - board.width();
        int offsetY = (app.heightInChars() - board.height())/2;

        String scoreDisplay = String.format("Mines: %s    Flags: %s", board.mineCount(), board.flagCount());
        app.getTerminal().write(scoreDisplay, (board.width() - 11) + offsetX, offsetY - 3, Color.YELLOW);

        for (int i_y = 0; i_y < board.height(); i_y++) {
            for (int i_x = 0; i_x < board.width(); i_x++) {

                String display = getDisplayCharForTileAt(i_x, i_y);
                Color bgColor = app.getTerminal().getDefaultBackgroundColor();
                if (i_x == cursor.x && i_y == cursor.y) { bgColor = cursorBgColor; }

                app.getTerminal().write(display, (i_x * 2) + offsetX, (i_y) + offsetY, colorForTile.get(display), bgColor);
            }
        }
    }

    private String getDisplayCharForTileAt(int x, int y) {
        if (board.isFlaggedAt(x, y))          { return flagChar; }
        if (!board.isRevealedAt(x, y))        { return unrevealedTileChar; }
        if (board.adjacentMinesAt(x, y) == 0) { return emptyTileChar; }
        return String.valueOf(board.adjacentMinesAt(x, y));
    }

    private void processInputs(ArrayList<KeyEvent> inputs) {
        ScreenedApplication application = application();

        for (KeyEvent input : inputs) {
            int keyCode = input.getKeyCode();

            if (keyCode == KeyEvent.VK_W) {
                moveCursorUp();
            } else if (keyCode == KeyEvent.VK_S) {
                moveCursorDown();
            } else if (keyCode == KeyEvent.VK_D) {
                moveCursorRight();
            } else if (keyCode == KeyEvent.VK_A) {
                moveCursorLeft();
            } else if (keyCode == KeyEvent.VK_F) {
                if (!board.isRevealedAt(cursor.x, cursor.y)) {
                    board.toggleFlagAt(cursor.x, cursor.y);
                }
            } else if (keyCode == KeyEvent.VK_ENTER) {
                if (cannotRevealTileAtCursor()) {
                    return;
                } else if (board.hasMineAt(cursor.x, cursor.y)) {
                    Screens.goToGameFinishScreen(application, "You lost");
                }
                board.startRevealAt(cursor.x, cursor.y);
            } else if (input.getKeyCode() == KeyEvent.VK_ESCAPE) {
                application.setScreen(Screens.PAUSE);
            }
        }
    }

    private boolean cannotRevealTileAtCursor() {
        return board.isFlaggedAt(cursor.x, cursor.y) || board.isRevealedAt(cursor.x, cursor.y);
    }

    private void moveCursorLeft() {
        if (cursor.x > 0) {
            cursor.x -= 1;
        }
    }

    private void moveCursorRight() {
        if (cursor.x < board.width() - 1) {
            cursor.x += 1;
        }
    }

    private void moveCursorDown() {
        if (cursor.y < board.height() - 1) {
            cursor.y += 1;
        }
    }

    private void moveCursorUp() {
        if (cursor.y > 0) {
            cursor.y -= 1;
        }
    }
}
