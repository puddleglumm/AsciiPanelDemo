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
                } else if (input.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    application.setScreen(3);
                }
            }
            writePlayerPiece(terminal, 30 + (3 * (currentSelection + 1)) - 2, 2, turn);
        } else {
            if (animationProgress == board.length - 1 || board[animationProgress + 1][currentSelection] != 0) {
                board[animationProgress][currentSelection] = turn;
                if (checkWin(currentSelection,animationProgress)) {
                    SelectionScreen winScreen = (SelectionScreen) application.getScreen(2);
                    if (turn == -1) {
                        winScreen.setDisplayTitle("Yellow wins!");
                    } else {
                        winScreen.setDisplayTitle("Red wins!");
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
        /*boolean xibl = x > 2;
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
        return false;*/
        int consecutive_NE = 1;
        int consecutive_SE = 1;
        int consecutive_N = 1;
        int consecutive_E = 1;
        int last_NE = 0;
        int last_SE = 0;
        int last_N = 0;
        int last_E = 0;
        for (int i = -3; i < 4; i++) {
            // E
            if (0 <= x + i && x + i < board[0].length) {
                if (board[y][x + i] == last_E) {
                    consecutive_E++;
                    if (consecutive_E == 4 && last_E != 0) {
                        return true;
                    }
                } else { last_E = board[y][x + i]; consecutive_E = 1; }
                // NE
                if (0 <= y + i && y + i < board.length) {
                    if (board[y + i][x + i] == last_NE) {
                        consecutive_NE++;
                        if (consecutive_NE == 4 && last_NE != 0) {
                            return true;
                        }
                    } else { last_NE = board[y + i][x + i]; consecutive_NE = 1; }
                }
                // SE
                if (0 <= y - i && y - i < board.length) {
                    if (board[y - i][x + i] == last_SE) {
                        consecutive_SE++;
                        if (consecutive_SE == 4 && last_SE != 0) {
                            return true;
                        }
                    } else { last_SE = board[y - i][x + i]; consecutive_SE = 1; }
                }
            }
            // N
            if (0 <= y + i && y + i < board.length) {
                if (board[y + i][x] == last_N) {
                    consecutive_N++;
                    if (consecutive_N == 4 && last_N != 0) {
                        return true;
                    }
                } else { last_N = board[y + i][x]; consecutive_N = 1; }

            }
        }

        return false;
    }
}
