package ru.coungard.fractal.elements;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class MenuItem extends StackPane {

    public MenuItem(String name) {
        Rectangle rect = new Rectangle(300,30, Color.WHITE);
        rect.setOpacity(0.8);
        Text text = new Text(name);
        text.setFont(Font.font("Arial", FontWeight.BOLD,20));

        getChildren().addAll(rect,text);

        FillTransition ft = new FillTransition(Duration.seconds(0.5),rect);

        setOnMouseEntered(event -> {
            ft.setFromValue(Color.LAVENDER);
            ft.setToValue(Color.DARKGOLDENROD);
            ft.setCycleCount(Animation.INDEFINITE);
            ft.setAutoReverse(true);
            ft.play();
        });

        setOnMouseExited(event -> {
            ft.stop();
            rect.setFill(Color.WHITE);
        });
    }
}
