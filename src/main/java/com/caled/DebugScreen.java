package com.caled;

import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class DebugScreen implements Screen {
    private int spaceBetweenOptions = 1;
    private Selection[] optionsList;
    private String displayTitle;
    int currentSelection = 0;
    int x;
    int y;
    private String[] keyCodeToChar = new String[]{"a","b","c","d","e","f","g","h","i","j", "k","l","m",
                                                  "n","o","p","q", "r","s","t","u","v","w","x","y","z"};
    String keyStrokes = "";

    DebugScreen(Selection[] options, String title, ScreenedApplication app, int spacing) {
        spaceBetweenOptions = spacing;
        optionsList = options;
        x = app.getTerminal().getWidthInCharacters() / 2;
        y = (app.getTerminal().getHeightInCharacters() - ((optionsList.length + 2) * spaceBetweenOptions)) / 2;
        displayTitle = title;
    }

    DebugScreen(Selection[] options, String title, ScreenedApplication app) {
        optionsList = options;
        x = app.getTerminal().getWidthInCharacters() / 2;
        y = (app.getTerminal().getHeightInCharacters() - ((optionsList.length + 2) * spaceBetweenOptions)) / 2;
        displayTitle = title;
    }

    public void tick(ScreenedApplication application, ArrayList<KeyEvent> inputs) {
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();
        for (KeyEvent input : inputs) {
            if (65 <= input.getKeyCode() && input.getKeyCode() <= 90) {
                System.out.println(keyStrokes);
                keyStrokes += keyCodeToChar[input.getKeyCode() - 65];
            }
            if (48 <= input.getKeyCode() && input.getKeyCode() <= 57) {
                System.out.println(keyStrokes);
                keyStrokes += String.valueOf(input.getKeyCode() - 48);
            }
            if (input.getKeyCode() == KeyEvent.VK_SPACE) {
                keyStrokes += " ";
            }
            if ((input.getKeyCode() == KeyEvent.VK_BACK_SPACE || input.getKeyCode() == KeyEvent.VK_DELETE) && keyStrokes.length() > 0) {
                keyStrokes = keyStrokes.substring(0, keyStrokes.length() - 1);
            }

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
        terminal.write(keyStrokes, 0, 0, Color.ORANGE);
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
        optionsList[selectionIndex].onInteract(application);
    }

    public void setDisplayTitle(String title) {
        displayTitle = title;
    }

    public void reset() {
        currentSelection = 0;
    }
}
