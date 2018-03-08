package ru.coungard.fractal.utils;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import ru.coungard.fractal.main.Main;

public class MusicUtil {

    private MyMusicPlayer myPlayer;
    private static final String musicUrl =
            "src/main/resources/music/Royksopp - Goodnite Mr. Sweetheart.mp3";


    public void addPlayer() {
        Pane root = Main.mainRoot;

        Image image1 = new Image("images/play.png");
        ImageView playImg = new ImageView(image1);
        playImg.setTranslateX(190);
        playImg.setTranslateY(450);

        Image image2 = new Image("images/pause.png");
        ImageView pauseImg = new ImageView(image2);
        pauseImg.setTranslateX(190);
        pauseImg.setTranslateY(450);
        pauseImg.setVisible(false);

        playImg.setOnMouseClicked(event -> {
            playImg.setVisible(false);
            pauseImg.setVisible(true);
            playMusic();
        });

        pauseImg.setOnMouseClicked(event -> {
            pauseImg.setVisible(false);
            playImg.setVisible(true);
            myPlayer.close();
        });

        root.getChildren().addAll(playImg, pauseImg);
    }

    private void playMusic() {
        myPlayer = new MyMusicPlayer(musicUrl, true);
        myPlayer.start();
    }
}
