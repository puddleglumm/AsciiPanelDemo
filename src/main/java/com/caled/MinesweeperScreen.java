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
    HashMap<String, Color> colorForTile = new HashMap<String, Color>() {{
        put("#", Color.LIGHT_GRAY);
        put(".", Color.DARK_GRAY);
        put("1", Color.BLUE);
        put("2", Color.GREEN);
        put("3", Color.RED);
        put("4", new Color(0, 0, 120));
        put("5", new Color(120, 0, 0));
        put("6", new Color(0, 110, 110));
        put("7", Color.BLACK);
        put("8", Color.DARK_GRAY);
        put("\u00E2", Color.ORANGE);
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
            SelectionScreen winScreen = (SelectionScreen) application.getScreen(Screens.GAME_FINISH);
            winScreen.setDisplayTitle("You won!");
            application.setScreen(Screens.GAME_FINISH);
        }

        renderBoard(terminal);
        terminal.repaint();
    }

    @Override
    public void reset() {
        board = new Board(20, 20, 21);
        cursorX = 0;
        cursorY = 0;
    }

    private void renderBoard(AsciiPanel terminal) {
        for (int i_y = 0; i_y < board.height(); i_y++) {
            for (int i_x = 0; i_x < board.width(); i_x++) {

                int offsetX = terminal.getWidthInCharacters()/2 - board.width();
                int offsetY = (terminal.getHeightInCharacters() - board.height())/2;

                String display = getDisplayCharForTileAt(i_x, i_y);
                Color bgColor = terminal.getDefaultBackgroundColor();
                if (i_x == cursorX && i_y == cursorY) { bgColor = new Color(160, 130, 0); }

                terminal.write(display, (i_x * 2) + offsetX, (i_y) + offsetY, colorForTile.get(display), bgColor);
            }
        }
    }

    private String getDisplayCharForTileAt(int x, int y) {
        if (board.isFlaggedAt(x, y))          { return "\u00E2"; }
        if (!board.isRevealedAt(x, y))        { return "#"; }
        if (board.adjacentMinesAt(x, y) == 0) { return "."; }
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
                    SelectionScreen loseScreen = (SelectionScreen) application.getScreen(Screens.GAME_FINISH);
                    loseScreen.setDisplayTitle("You lost");
                    application.setScreen(Screens.GAME_FINISH);
                } else if (!board.isRevealedAt(cursorX, cursorY) && !board.isFlaggedAt(cursorX, cursorY)) {
                    board.startRevealAt(cursorX, cursorY);
                }
            } else if (input.getKeyCode() == KeyEvent.VK_ESCAPE) {
                application.setScreen(Screens.PAUSE);
            }
        }
    }
}
