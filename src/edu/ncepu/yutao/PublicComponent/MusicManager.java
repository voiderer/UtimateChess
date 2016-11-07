package edu.ncepu.yutao.PublicComponent;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.IOException;

/**
 * Created by yutao on 16-6-20.
 */
public class MusicManager extends Thread {
    static MusicManager manager = null;
    static boolean isPlaying;
    static Player player;
    static String resource;

    public static void PlayMusic(String name) {
        resource = name;
        isPlaying = true;
        new MusicManager().start();
    }

    public static boolean switchMusic() {
        if (isPlaying) {
            player.close();
            isPlaying = false;
            return true;
        } else {
            isPlaying = true;
            new MusicManager().start();
            return false;
        }
    }

    @Override
    public void run() {
        while (isPlaying) {
            try {
                BufferedInputStream inputStream = new BufferedInputStream(MusicManager.class.getResourceAsStream(resource));
                player = new Player(inputStream);
                player.play();
                inputStream.close();
            } catch (JavaLayerException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
