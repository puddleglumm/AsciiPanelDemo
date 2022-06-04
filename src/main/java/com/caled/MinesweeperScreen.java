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

        // TODO: create checkWin() function
        /*if (board.checkWin()) {
            SelectionScreen winScreen = (SelectionScreen) application.getScreen(Screens.GAME_FINISH);
            winScreen.setDisplayTitle("You won!");
            application.setScreen(Screens.GAME_FINISH);
        }*/

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
        for (int i_y = 0; i_y < board.height(); i_y++) {
            for (int i_x = 0; i_x < board.width(); i_x++) {
                Color textColor = Color.WHITE;
                if (i_x == cursorX && i_y == cursorY) { textColor = Color.GREEN; }
                terminal.write(getDisplayCharForTileAt(i_x, i_y), (i_x * 2) + 10, (i_y), textColor);
            }
        }
    }

    private String getDisplayCharForTileAt(int x, int y) {
        // TODO: implement isFlaggedAt(x, y)
        //if (board.isFlaggedAt(x, y))          { return "P"; }
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
                // TODO: implements toggleFlagAt(x, y)
                //board.toggleFlagAt(cursorX, cursorY);
            } else if (keyCode == KeyEvent.VK_ENTER) {
                if (board.hasMineAt(cursorX, cursorY)) {
                    SelectionScreen loseScreen = (SelectionScreen) application.getScreen(Screens.GAME_FINISH);
                    loseScreen.setDisplayTitle("You lost");
                    application.setScreen(Screens.GAME_FINISH);
                // TODO: isFlaggedAt(x, y)
                } else if (!board.isRevealedAt(cursorX, cursorY) /*&& !board.isFlaggedAt(cursorX, cursorY)*/) {
                    board.playerInteractAt(cursorX, cursorY);
                }
            } else if (input.getKeyCode() == KeyEvent.VK_ESCAPE) {
                application.setScreen(Screens.PAUSE);
            }
        }
    }
}
