package com.caled;

public class Screens {
    public static final int HOME = 0;
    public static final int CONNECT_4 = 1;
    public static final int GAME_FINISH = 2;
    public static final int PAUSE = 3;
    public static final int KILL = -1;

    public static void initializeScreens(ScreenedApplication app) {

        Selection[] homeScreen = new Selection[]{
                new redirectSelection("[   play   ]", Screens.CONNECT_4, new int[]{Screens.CONNECT_4}),
                new TextPickerSelection("game", new String[]{"connect 4", "minesweeper", "snake"}),
                new redirectSelection("[   quit   ]", Screens.KILL, new int[]{})
        };

        Selection[] gameFinishScreen = new Selection[]{
                new redirectSelection("[   play again    ]", Screens.CONNECT_4, new int[]{Screens.CONNECT_4}),
                new TextPickerSelection("game", new String[]{"connect 4", "minesweeper", "snake"}),
                new redirectSelection("[ return to title ]", Screens.HOME, new int[]{Screens.CONNECT_4})
        };

        Selection[] pauseScreen = new Selection[]{
                new redirectSelection("[    continue     ]", Screens.CONNECT_4),
                new redirectSelection("[     restart     ]", Screens.CONNECT_4, new int[]{Screens.CONNECT_4}),
                new redirectSelection("[ return to title ]", Screens.HOME)
        };

        app.screens.add(new SelectionScreen(homeScreen, "Caleb's Game Collection", app, 2));
        app.screens.add(new ConnectFourScreen());
        app.screens.add(new SelectionScreen(gameFinishScreen, "placeholder", app, 2));
        app.screens.add(new SelectionScreen(pauseScreen, "Paused", app, 2));
    }
}
