package com.caled;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import asciiPanel.AsciiPanel;
import com.github.puddleglumm.minesweeper.*;

public class MinesweeperScreen implements Screen {

    Board board = new Board();
    int cursorX = 0;
    int cursorY = 0;

    @Override
    public void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();

        if (!board.revealInProgress()) {
            processInputs(application, inputs);
        } else {
            board.continueRevealing();
        }

        renderBoard(terminal);

        terminal.repaint();
    }

    @Override
    public void reset() {
        board = new Board();
        cursorX = 0;
        cursorY = 0;
    }

    private void renderBoard(AsciiPanel terminal) {
        for (int i_y = 0; i_y < 9 /*board.getHeight()*/; i_y++) {
            for (int i_x = 0; i_x < 9 /*board.getWidth()*/; i_x++) {
                Color textColor = Color.WHITE;
                if (i_x == cursorX && i_y == cursorY) { textColor = Color.YELLOW; }
                terminal.write(displayTileAt(i_x, i_y), i_x, i_y, textColor);
            }
        }
    }

    private String displayTileAt(int x, int y) {
        if (board.isRevealedAt(x, y)) {
            int adjacentMines = board.adjacentMinesAt(x, y);

            if (adjacentMines == 0) {
                return ".";
            } else {
                return String.valueOf(adjacentMines);
            }
        } else {
            return "#";
        }
    }

    private void processInputs(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        for (KeyEvent input : inputs) {
            int keyCode = input.getKeyCode();

            if (keyCode == KeyEvent.VK_W) {
                cursorY -= 1;
                if (cursorY < 0 /*board.getHeight()*/) {
                    cursorY = 9 /*board.getHeight()*/ - 1;
                }
            } else if (keyCode == KeyEvent.VK_S) {
                cursorY += 1;
                if (cursorY >= 9 /*board.getHeight()*/) {
                    cursorY = 0;
                }
            } else if (keyCode == KeyEvent.VK_D) {
                cursorX += 1;
                if (cursorX >= 9 /*board.getWidth()*/) {
                    cursorX = 0;
                }
            } else if (keyCode == KeyEvent.VK_A) {
                cursorX -= 1;
                if (cursorX < 0) {
                    cursorX = 9 /*board.getWidth()*/ - 1;
                }
            } else if (keyCode == KeyEvent.VK_ENTER) {
                if (board.hasMineAt(cursorX, cursorY)) {
                    SelectionScreen loseScreen = (SelectionScreen) application.getScreen(Screens.GAME_FINISH);
                    loseScreen.setDisplayTitle("You lost");
                    application.setScreen(Screens.GAME_FINISH);
                } else if (!board.isRevealedAt(cursorX, cursorY)) {
                    board.playerInteractAt(cursorX, cursorY);
                }
            }
        }
    }
}
