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
                new textPickerSelection("game", new String[]{"connect 4", "minesweeper", "snake"}),
                new redirectSelection("debug", Screens.DEBUG),
                new redirectSelection("quit", Screens.KILL)
        };

        Selection[] gameFinishScreen = new Selection[]{
                new redirectSelection("play again", Screens.LAST, Screens.LAST),
                new redirectSelection("return to title", Screens.HOME, Screens.LAST)
        };

        Selection[] debugScreen = new Selection[]{
                new textPickerSelection("string", new String[]{"opt. 1", "opt. 2", "opt. 3"}),
                new numberPickerSelection("numbers", 0, 10),
                new TextBoxSelection("input", "put stuff here"),
                new redirectSelection("return to title", Screens.HOME)
        };

        Selection[] pauseScreen = new Selection[]{
                new redirectSelection("continue", Screens.LAST),
                new redirectSelection("restart", Screens.LAST, Screens.LAST),
                new redirectSelection("return to title", Screens.HOME)
        };

        app.screens.add(new SelectionScreen(homeScreen, "Caleb's Game Collection", app, 2));
        app.screens.add(new ConnectFourScreen());
        app.screens.add(new SelectionScreen(gameFinishScreen, "placeholder", app, 2));
        app.screens.add(new SelectionScreen(pauseScreen, "Paused", app, 2));
        app.screens.add(new DebugScreen(debugScreen, "debug", app, 2));
        app.screens.add(new MinesweeperScreen());
        app.screens.add(new SnakeScreen());
    }

    public static void goToGameFinishScreen(ScreenedApplication app, String title) {
        SelectionScreen winScreen = (SelectionScreen) app.getScreen(Screens.GAME_FINISH);
        winScreen.setDisplayTitle(title);
        app.setScreen(Screens.GAME_FINISH);
    }
}
