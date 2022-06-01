package com.caled;

import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

public class SelectionScreen implements Screen {
    private int spaceBetweenOptions = 1;
    private String[] optionsList;
    private String dispalyTitle;
    int currentSelection = 0;
    int x;
    int y;

    SelectionScreen(String[] options, String title, ScreenedApplication app, int spacing) {
        spaceBetweenOptions = spacing;
        optionsList = options;
        x = app.getTerminal().getWidthInCharacters() / 2;
        y = (app.getTerminal().getHeightInCharacters() - (optionsList.length * spaceBetweenOptions)) / 2;
        dispalyTitle = title;
    }
    SelectionScreen(String[] options, String title, ScreenedApplication app) {
        optionsList = options;
        x = app.getTerminal().getWidthInCharacters() / 2;
        y = (app.getTerminal().getHeightInCharacters() - (optionsList.length * spaceBetweenOptions)) / 2;
        dispalyTitle = title;
    }

    public void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();
        for (KeyEvent input : inputs) {
            if (input.getKeyCode() == KeyEvent.VK_W) {
                currentSelection -= 1;
                if (currentSelection < 0) {
                    currentSelection = optionsList.length - 1;
                }
            } else if (input.getKeyCode() == KeyEvent.VK_S) {
                currentSelection += 1;
                if (currentSelection >= optionsList.length) {
                    currentSelection = 0;
                }
            } else if (input.getKeyCode() == KeyEvent.VK_ENTER) {
                onSelection(currentSelection, application);
            }
        }
        int yPosition = y;
        terminal.write(dispalyTitle, x - (dispalyTitle.length()/2), yPosition, Color.YELLOW);
        yPosition += spaceBetweenOptions;
        int i = 0;
        for (String option : optionsList) {
            if (i == currentSelection) {
                terminal.write(option, x - (option.length()/2), yPosition, Color.YELLOW);
            } else {
                terminal.write(option, x - (option.length()/2), yPosition, Color.WHITE);
            }
            yPosition += spaceBetweenOptions;
            i++;
        }
        terminal.repaint();
    }

    private void onSelection(int selectionIndex, ScreenedApplication application) {
        if (Objects.equals(optionsList[selectionIndex], "[   quit   ]")) {
            application.exit();
        } else if (Objects.equals(optionsList[selectionIndex], "[   play   ]")) {
            application.resetScreen(1);
            application.setScreen(1);
        } else if (Objects.equals(optionsList[selectionIndex], "[ return to title ]")) {
            application.setScreen(0);
        } else if (Objects.equals(optionsList[selectionIndex], "[   play again    ]")) {
            application.resetScreen(1);
            application.setScreen(1);
        } else if (Objects.equals(optionsList[selectionIndex], "[    continue     ]")) {
            application.setScreen(1);
        } else if (Objects.equals(optionsList[selectionIndex], "[     restart     ]")) {
            application.resetScreen(1);
            application.setScreen(1);
        }
    }

    public void setDispalyTitle(String title) {
        dispalyTitle = title;
    }

    public void reset() {
        currentSelection = 0;
    }
}
