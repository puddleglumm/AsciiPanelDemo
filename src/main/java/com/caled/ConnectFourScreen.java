package com.caled;

import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class ConnectFourScreen implements Screen {
    int[][] board = new int[6][7];
    int currentSelection = 0;
    int turn = 1;
    int animationProgress = -1;


    ConnectFourScreen() {
        for (int[] ints : board) {
            Arrays.fill(ints, 0);
        }
    }
    public void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();
        if (animationProgress < 0) {
            for (KeyEvent input : inputs) {
                if (input.getKeyCode() == KeyEvent.VK_A) {
                    currentSelection -= 1;
                    if (currentSelection < 0) {
                        currentSelection = board[0].length - 1;
                    }
                } else if (input.getKeyCode() == KeyEvent.VK_D) {
                    currentSelection += 1;
                    if (currentSelection >= board[0].length) {
                        currentSelection = 0;
                    }
                } else if (input.getKeyCode() == KeyEvent.VK_ENTER && board[0][currentSelection] == 0) {
                    animationProgress = 0;
                }
            }
            writePlayerPiece(terminal, 30 + (3 * (currentSelection + 1)) - 2, 2, turn);
        } else {
            if (animationProgress == board.length - 1 || board[animationProgress + 1][currentSelection] != 0) {
                board[animationProgress][currentSelection] = turn;
                if (checkWin(currentSelection,animationProgress)) {
                    SelectionScreen winScreen = (SelectionScreen) application.getScreen(2);
                    if (turn == -1) {
                        winScreen.setDispalyTitle("Yellow wins!");
                    } else {
                        winScreen.setDispalyTitle("Red wins!");
                    }
                    application.setScreen(2);
                } else { animationProgress = -1; turn *= -1; }

            } else {
                writePlayerPiece(terminal, 30 + (3 * (currentSelection + 1)) - 2, 4 + (3 * (animationProgress + 1)) - 2, turn);
                if (animationProgress < board.length - 1) { animationProgress++; }
            }
        }
        int i = 0;
        for (int[] row : board) {
            int j = 0;
            for (int piece : row) {
                writePlayerPiece(terminal, 30 + (3 * (j + 1)) - 2, 4 + (3 * (i + 1)) - 2, piece);
                j++;
            }
            i++;
        }
        drawConnectFourGrid(terminal, 30, 4);
        terminal.repaint();
    }

    private void writePlayerPiece(AsciiPanel terminal, int x, int y, int player) {
        if (player == -1)  {
            terminal.write("@", x, y, Color.yellow);
            terminal.write("@", x + 1, y, Color.yellow);
            terminal.write("@", x, y + 1, Color.yellow);
            terminal.write("@", x + 1, y + 1, Color.yellow);
        }
        else if (player == 1) {
            terminal.write("@", x, y, Color.red);
            terminal.write("@", x + 1, y, Color.red);
            terminal.write("@", x, y + 1, Color.red);
            terminal.write("@", x + 1, y + 1, Color.red);
        }

    }

    private void drawConnectFourGrid(AsciiPanel terminal, int x, int y) {
        for (int i = 0; i < board.length + 1; i++) {
            for (int j = 0; j < (3 * (board[0].length + 1)) - 2; j++) {
                terminal.write("#", x + j, y + (i * 3));
            }
            if (i < board.length) {
                for (int j = 0; j < board.length + 2; j++) {
                    terminal.write("#", x + (j * 3), 1 + y + (i * 3));
                    terminal.write("#", x + (j * 3), 2 + y + (i * 3));
                }
            }
        }
    }

    public void reset() {
        this.board = new int[6][7];
        this.currentSelection = 0;
        this.turn = 1;
        this.animationProgress = -1;
        for (int[] ints : this.board) {
            Arrays.fill(ints, 0);
        }
    }

    private boolean checkWin(int x, int y) {
        boolean xibl = x > 2;
        boolean yibu = y > 3;
        boolean yibd = y < 3;

        if (xibl) {
            if (Math.abs(board[y][x - 1] + board[y][x - 2] + board[y][x - 3] + board[y][x]) == 4) { return true; }
            else if (yibu) {
                if (Math.abs(board[y - 1][x - 1] + board[y - 2][x - 2] + board[y - 3][x - 3] + board[y][x]) == 4) { return true; }
            } else if (yibd) {
                if (Math.abs(board[y + 1][x - 1] + board[y + 2][x - 2] + board[y + 3][x - 3] + board[y][x]) == 4) { return true; }
            }
        } else {
            if (Math.abs(board[y][x + 1] + board[y][x + 2] + board[y][x + 3] + board[y][x]) == 4) { return true; }
            else if (yibu) {
                if (Math.abs(board[y - 1][x + 1] + board[y - 2][x + 2] + board[y - 3][x + 3] + board[y][x]) == 4) { return true; }
            } else if (yibd) {
                if (Math.abs(board[y + 1][x + 1] + board[y + 2][x + 2] + board[y + 3][x + 3] + board[y][x]) == 4) { return true; }
            }
        }
        if (yibu) {
            if (Math.abs(board[y - 1][x] + board[y - 2][x] + board[y - 3][x] + board[y][x]) == 4) { return true; }
        } else if (yibd) {
            if (Math.abs(board[y + 1][x] + board[y + 2][x] + board[y + 3][x] + board[y][x]) == 4) { return true; }
        }
        return false;
    }
}
