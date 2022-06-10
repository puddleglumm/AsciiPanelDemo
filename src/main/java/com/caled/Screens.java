package com.caled;

public class Screens {
    public static final int HOME = 0;
    public static final int CONNECT_4 = 1;
    public static final int GAME_FINISH = 2;
    public static final int PAUSE = 3;
    public static final int DEBUG = 4;
    public static final int MINESWEEPER = 5;
    public static final int SNAKE = 6;
    public static final int KILL = -1;
    public static final int LAST = -2;

    public static void initializeScreens(ScreenedApplication app) {

        Selection[] homeScreen = new Selection[]{
                new redirectSelection("play connect 4", Screens.CONNECT_4, Screens.CONNECT_4),
                new redirectSelection("play minesweeper", Screens.MINESWEEPER, Screens.MINESWEEPER),
                new redirectSelection("play snake", Screens.SNAKE, Screens.SNAKE),
                new ListPickerSelection("game", new String[]{"connect 4", "minesweeper", "snake"}),
                new redirectSelection("debug", Screens.DEBUG),
                new redirectSelection("quit", Screens.KILL)
        };

        Selection[] gameFinishScreen = new Selection[]{
                new redirectSelection("play again", Screens.LAST, Screens.LAST),
                new redirectSelection("return to title", Screens.HOME, Screens.LAST)
        };

        Selection[] debugScreen = new Selection[]{
                new ListPickerSelection("string", new String[]{"opt. 1", "opt. 2", "opt. 3"}),
                new ListPickerSelection("range", ListPickerSelection.stringArrayFromIntRange(0, 20)),
                new TextBoxSelection("input", "put stuff here"),
                new ListPickerSelection("tempMines", ListPickerSelection.stringArrayFromIntRange(0, 20), MinesweeperScreen.MINECOUNT_PROPERTY_NAME),
                new redirectSelection("return to title", Screens.HOME)
        };

        Selection[] pauseScreen = new Selection[]{
                new redirectSelection("continue", Screens.LAST),
                new redirectSelection("restart", Screens.LAST, Screens.LAST),
                new redirectSelection("return to title", Screens.HOME)
        };

        app.screens.add(new SelectionScreen(app, homeScreen, "Caleb's Game Collection", 2));
        app.screens.add(new ConnectFourScreen(app));
        app.screens.add(new SelectionScreen(app, gameFinishScreen, "placeholder", 2));
        app.screens.add(new SelectionScreen(app, pauseScreen, "Paused", 2));
        app.screens.add(new DebugScreen(app, debugScreen, "debug", 2));
        app.screens.add(new MinesweeperScreen(app));
        app.screens.add(new SnakeScreen(app));
    }

    public static void goToGameFinishScreen(ScreenedApplication app, String title) {
        SelectionScreen winScreen = (SelectionScreen) app.getScreen(Screens.GAME_FINISH);
        winScreen.setDisplayTitle(title);
        app.setScreen(Screens.GAME_FINISH);
    }
}
