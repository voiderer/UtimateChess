package edu.ncepu.yutao.Chess;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;


/**
 * Created by AUTOY on 2016/5/25.
 */
public class SoundThread extends Thread {
    private static boolean enable = true;
    private String resource;

    public SoundThread(String resource) {
        this.resource = resource;
        this.start();
    }

    public static void playOnce(String resource) {
        if (enable) {
            new SoundThread(resource);
        }
    }

    public static boolean switchEnable() {
        return enable = !enable;
    }

    public void run() {
        try {
            AudioStream as = new AudioStream(SoundThread.class.getResourceAsStream(resource));
            AudioPlayer.player.start(as);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


