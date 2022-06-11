package com.caled;

import javax.sound.sampled.*;

public class Sounds {
    public static String SNAKE_MUNCH = "snake_munch.wav";
    public static String SNAKE_BOINK = "snake_boink.wav";
    public static String MS_BOMB_EXPLOSION = "minesweeper_bomb.wav";

    public static synchronized void playSound(String url) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                    Sounds.class.getClassLoader().getResourceAsStream(url));
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
