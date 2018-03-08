package ru.coungard.fractal.utils;

import javazoom.jl.player.Player;

import java.io.FileInputStream;

public class MyMusicPlayer extends Thread{
    private String fileLocation;
    private boolean loop;
    private Player player;

    public MyMusicPlayer(String fileLocation, boolean loop) {
        this.fileLocation = fileLocation;
        this.loop = loop;
    }

    public void run() {

        try {
            do {
                FileInputStream input = new FileInputStream(fileLocation);
                player = new Player(input);
                player.play();
            } while (loop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(){
        loop = false;
        player.close();
        this.interrupt();
    }
}
