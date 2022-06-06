package com.caled;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asciiPanel.AsciiPanel;
import com.github.puddleglumm.minesweeper.*;

public class MinesweeperScreen implements Screen {

    Board board = new Board(20, 20, 21);
    int cursorX = 0;
    int cursorY = 0;
    private final String flagChar = "\u00E2";
    private final String unrevealedTileChar = "#";
    private final String emptyTileChar = ".";
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

    @Override
    public void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();

        if (!board.revealInProgress()) {
            processInputs(application, inputs);
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
        board = new Board(20, 20, 21);
        cursorX = 0;
        cursorY = 0;
    }

    private void renderBoard(ScreenedApplication app) {
        for (int i_y = 0; i_y < board.height(); i_y++) {
            for (int i_x = 0; i_x < board.width(); i_x++) {

                int offsetX = app.widthInChars()/2 - board.width();
                int offsetY = (app.heightInChars() - board.height())/2;

                String display = getDisplayCharForTileAt(i_x, i_y);
                Color bgColor = app.getTerminal().getDefaultBackgroundColor();
                if (i_x == cursorX && i_y == cursorY) { bgColor = new Color(160, 130, 0); }

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

    private void processInputs(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        for (KeyEvent input : inputs) {
            int keyCode = input.getKeyCode();

            if (keyCode == KeyEvent.VK_W) {
                cursorY -= 1;
                if (cursorY < 0) {
                    cursorY = board.height() - 1;
                }
            } else if (keyCode == KeyEvent.VK_S) {
                cursorY += 1;
                if (cursorY >= board.height()) {
                    cursorY = 0;
                }
            } else if (keyCode == KeyEvent.VK_D) {
                cursorX += 1;
                if (cursorX >= board.width()) {
                    cursorX = 0;
                }
            } else if (keyCode == KeyEvent.VK_A) {
                cursorX -= 1;
                if (cursorX < 0) {
                    cursorX = board.width() - 1;
                }
            } else if (keyCode == KeyEvent.VK_F) {
                if (!board.isRevealedAt(cursorX, cursorY)) {
                    board.toggleFlagAt(cursorX, cursorY);
                }
            } else if (keyCode == KeyEvent.VK_ENTER) {
                if (board.hasMineAt(cursorX, cursorY)) {
                    Screens.goToGameFinishScreen(application, "You lost");
                } else if (!board.isRevealedAt(cursorX, cursorY) && !board.isFlaggedAt(cursorX, cursorY)) {
                    board.startRevealAt(cursorX, cursorY);
                }
            } else if (input.getKeyCode() == KeyEvent.VK_ESCAPE) {
                application.setScreen(Screens.PAUSE);
            }
        }
    }
}
