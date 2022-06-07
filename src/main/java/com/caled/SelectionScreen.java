package com.caled;

import asciiPanel.AsciiPanel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SelectionScreen extends BasicScreen {
    private final String[] keyCodeToChar = new String[]{"a","b","c","d","e","f","g","h","i","j", "k","l","m",
            "n","o","p","q", "r","s","t","u","v","w","x","y","z"};
    private int spaceBetweenOptions = 1;
    private Selection[] optionsList;
    private String displayTitle;
    private int currentSelection = 0;
    private int x;
    private int y;

    SelectionScreen(ScreenedApplication app, Selection[] options, String title, int spacing) {
        super(app);
        spaceBetweenOptions = spacing;
        optionsList = options;
        x = app.widthInChars() / 2;
        y = (app.heightInChars() - ((optionsList.length + 2) * spaceBetweenOptions)) / 2;
        displayTitle = title;
    }

    SelectionScreen(ScreenedApplication app, Selection[] options, String title) {
        super(app);
        optionsList = options;
        x = app.widthInChars() / 2;
        y = (app.heightInChars() - ((optionsList.length + 2) * spaceBetweenOptions)) / 2;
        displayTitle = title;
    }

    public void tick(ArrayList<KeyEvent> inputs) {
        ScreenedApplication application = application();
        AsciiPanel terminal = application.getTerminal();
        terminal.clear();
        for (KeyEvent input : inputs) {
            if (application.typing) {
                TextBoxSelection selectedTB = (TextBoxSelection) optionsList[currentSelection];
                if (65 <= input.getKeyCode() && input.getKeyCode() <= 90) {
                    selectedTB.text += keyCodeToChar[input.getKeyCode() - 65];
                }
                if (48 <= input.getKeyCode() && input.getKeyCode() <= 57) {
                    selectedTB.text += String.valueOf(input.getKeyCode() - 48);
                }
                if (input.getKeyCode() == KeyEvent.VK_SPACE) {
                    selectedTB.text += " ";
                }
                if ((input.getKeyCode() == KeyEvent.VK_BACK_SPACE || input.getKeyCode() == KeyEvent.VK_DELETE) && selectedTB.text.length() > 0) {
                    selectedTB.text = selectedTB.text.substring(0, selectedTB.text.length() - 1);
                }
                if (input.getKeyCode() == KeyEvent.VK_ESCAPE) { application.typing = false; }
            }
            else {
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
                    onSelection(currentSelection);
                } else if (input.getKeyCode() == KeyEvent.VK_A) {
                    optionsList[currentSelection].onLeft();
                } else if (input.getKeyCode() == KeyEvent.VK_D) {
                    optionsList[currentSelection].onRight();
                }
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

    private void onSelection(int selectionIndex) {
        optionsList[selectionIndex].onInteract(application());
    }

    public void setDisplayTitle(String title) {
        displayTitle = title;
    }

    public void reset() {
        currentSelection = 0;
    }

    public Selection[] getSelections() {
        return optionsList;
    }
}
