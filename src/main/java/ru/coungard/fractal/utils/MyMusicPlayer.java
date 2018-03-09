package ru.coungard.fractal.utils;

import javazoom.jl.player.Player;

import java.io.FileInputStream;

/* Проигрыватель mp3 (не без сторонней библиотеки, для простоты) с возможностью повторного
воспроизведения композиции.
 */
public class MyMusicPlayer extends Thread{
    private String fileLocation;
    private boolean loop;
    private Player player;

    MyMusicPlayer(String fileLocation, boolean loop) {
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

    void close(){
        loop = false;
        player.close();
        this.interrupt();
    }
}
