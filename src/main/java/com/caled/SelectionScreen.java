package com.caled;

import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Objects;

public class SelectionScreen implements Screen {
    private int spaceBetweenOptions = 1;
    private Selection[] optionsList;
    private String displayTitle;
    int currentSelection = 0;
    int x;
    int y;

    SelectionScreen(Selection[] options, String title, ScreenedApplication app, int spacing) {
        spaceBetweenOptions = spacing;
        optionsList = options;
        x = app.getTerminal().getWidthInCharacters() / 2;
        y = (app.getTerminal().getHeightInCharacters() - ((optionsList.length + 2) * spaceBetweenOptions)) / 2;
        displayTitle = title;
    }
    @Deprecated
    SelectionScreen(String[] options, String title, ScreenedApplication app, int spacing) {
        spaceBetweenOptions = spacing;
        Selection[] optionSelections = new Selection[options.length];
        for (int i = 0; i < options.length; i++) {
            optionSelections[i] = new redirectSelection(options[i], Screens.HOME);
        }
        optionsList = optionSelections;
        x = app.getTerminal().getWidthInCharacters() / 2;
        y = (app.getTerminal().getHeightInCharacters() - ((optionsList.length + 2) * spaceBetweenOptions)) / 2;
        displayTitle = title;
    }
    SelectionScreen(Selection[] options, String title, ScreenedApplication app) {
        optionsList = options;
        x = app.getTerminal().getWidthInCharacters() / 2;
        y = (app.getTerminal().getHeightInCharacters() - ((optionsList.length + 2) * spaceBetweenOptions)) / 2;
        displayTitle = title;
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
            } else if (input.getKeyCode() == KeyEvent.VK_A) {
                optionsList[currentSelection].onLeft();
            } else if (input.getKeyCode() == KeyEvent.VK_D) {
                optionsList[currentSelection].onRight();
            }
        }
        int yPosition = y;
        terminal.write(displayTitle, x - (displayTitle.length()/2), yPosition, Color.YELLOW);
        yPosition += spaceBetweenOptions * 2;
        int i = 0;
        for (Selection option : optionsList) {
            if (i == currentSelection) {
                terminal.write(option.display(), x - (option.display().length()/2), yPosition, Color.YELLOW);
            } else {
                terminal.write(option.display(), x - (option.display().length()/2), yPosition, Color.WHITE);
            }
            yPosition += spaceBetweenOptions;
            i++;
        }
        terminal.repaint();
    }

    private void onSelection(int selectionIndex, ScreenedApplication application) {
        /*String selectionDisplay = optionsList[selectionIndex].display();
        if (Objects.equals(selectionDisplay, "[   quit   ]")) {
            application.exit();
        } else if (Objects.equals(selectionDisplay, "[   play   ]")) {
            application.resetScreen(1);
            application.setScreen(1);
        } else if (Objects.equals(selectionDisplay, "[ return to title ]")) {
            application.setScreen(0);
        } else if (Objects.equals(selectionDisplay, "[   play again    ]")) {
            application.resetScreen(1);
            application.setScreen(1);
        } else if (Objects.equals(selectionDisplay, "[    continue     ]")) {
            application.setScreen(1);
        } else if (Objects.equals(selectionDisplay, "[     restart     ]")) {
            application.resetScreen(1);
            application.setScreen(1);
        } else {
           optionsList[selectionIndex].onInteract(application);
        }*/
        optionsList[selectionIndex].onInteract(application);
    }

    public void setDisplayTitle(String title) {
        displayTitle = title;
    }

    public void reset() {
        currentSelection = 0;
    }
}
