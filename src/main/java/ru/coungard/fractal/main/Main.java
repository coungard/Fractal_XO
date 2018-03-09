package ru.coungard.fractal.main;

import ru.coungard.fractal.elements.Block;
import ru.coungard.fractal.elements.MenuItem;
import ru.coungard.fractal.elements.Tile;
import ru.coungard.fractal.utils.MusicUtil;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

/*  Главный класс проекта, в котором инициализируется сцена, с сопутствующим добавлением в
нее элементов игры. Также класс отвечает за функционал с логикой перемещения из блока в блок,
установки наложения эффектов и т.д.(Что не совсем соответстует OOP)
 */
public class Main extends Application {
    public static Block[][] blocks = new Block[3][3];
    public static Block blockForFill;

    public static Pane gameRoot;
    public static Pane mainRoot;

    private MusicUtil musicUtil = new MusicUtil();

    private Pane launcher() {
        mainRoot = new Pane();
        mainRoot.setPrefSize(700, 700);

        Image image = new Image("images/background.jpg");
        ImageView background = new ImageView(image);
        background.setFitWidth(700);
        background.setFitHeight(700);

        VBox vBox = new VBox();
        vBox.setSpacing(50);
        vBox.setTranslateY(100);
        vBox.setTranslateX(200);

        MenuItem newGame = new MenuItem("New Game");
        MenuItem exit = new MenuItem("Exit");

        MenuItem backToMenu = new MenuItem("Back to Menu");
        backToMenu.setTranslateY(660);
        backToMenu.setTranslateX(200);
        backToMenu.setVisible(false);

        vBox.getChildren().addAll(newGame, exit);

        newGame.setOnMouseClicked(event -> {
            gameRoot = createGameRoot();
            gameRoot.setTranslateX(50);
            gameRoot.setTranslateY(50);
            mainRoot.getChildren().add(gameRoot);
            backToMenu.setVisible(true);
            vBox.setVisible(false);
            Tile.turnLabel.setVisible(true);
        });

        backToMenu.setOnMouseClicked(event -> {

            backToMenu.setVisible(false);
            vBox.setVisible(true);
            Tile.turnLabel.setText("");
            mainRoot.getChildren().remove(gameRoot);
        });

        exit.setOnMouseClicked(event -> System.exit(0));

        Tile.turnLabel.setTranslateX(20);
        Tile.turnLabel.setTranslateY(30);

        mainRoot.getChildren().addAll(background, vBox, backToMenu, Tile.turnLabel);

        musicUtil.addPlayer();

        return mainRoot;
    }

    // создание основного поля 3х3 из готовых блоков с тайлами
    private static Pane createGameRoot() {
        Pane gameRoot = new Pane();
        gameRoot.setPrefSize(600, 600);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Block block = new Block();
                block.setTranslateX(j * 200 + 10);
                block.setTranslateY(i * 200 + 10);

                gameRoot.getChildren().add(block);
                blocks[j][i] = block;
            }
        }

        Tile.playable = true;
        Tile.turnX = true;
        availability();
        blocks[1][1].setEffect(null);
        blocks[1][1].setDisable(false);
        return gameRoot;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(launcher());
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("FRACTAL");
        primaryStage.getIcons().add(new Image("images/mainIcon.jpg"));

        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }

    /*  Задача данного метода - это организация области видимости для хода игрока.
    Возможность хода после каждого действия обуславливается границами, заданными правилами
    игры. Хочу отметить, что статический blockForFill нам нужен для дальнейшей работы с ним
    из другого класса.(Закрашивание, занесение в список завершенных блоков и т.д.)
     */
    public static void availability() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Block currentBlock = blocks[j][i];
                setEffects(currentBlock);
                currentBlock.setDisable(true);

                currentBlock.setOnMouseClicked(event -> {
                    blockForFill = getAvailableBlock();
                    if (Tile.anchorStep) {
                        if (blockForFill.isOvercrowded()) {
                            blockForFill = getRandomBlock();
                            blockForFill.setEffect(null);
                            blockForFill.setDisable(false);
                        } else {
                            blockForFill.setEffect(null);
                            blockForFill.setDisable(false);
                        }
                    } else {
                        currentBlock.setEffect(null);
                        currentBlock.setDisable(false);
                    }
                });
            }
        }
    }

    // Mетод возвращает рандомный не переполненный блок.
    private static Block getRandomBlock() {
        Random rand = new Random();
        Block randomBlock;
        int attempt = 0; // is needed for the game ends, in order ot avoid recursion

        do {
            randomBlock = blocks[rand.nextInt(2)][rand.nextInt(2)];
            attempt++;
        } while (randomBlock.isOvercrowded() && attempt < 200);

        return randomBlock;
    }

    /*  Метод возвращает блок из общеигровой сетки 3х3 , которому соответствует тайл,
    находящийся в такой же позиции внутри определенного блока, по которому происходит
    нажатие.
     */
    private static Block getAvailableBlock() {
        Block availableBlock = null;

        label:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Block block = blocks[j][i];

                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        if (block.board[l][k] == Tile.currentTile) {
                            availableBlock = blocks[l][k];
                            break label;
                        }
                    }
                }
            }
        }
        return availableBlock;
    }

    private static void setEffects(Block block) {
        InnerShadow innerShadow = new InnerShadow();
        innerShadow.setOffsetX(4);
        innerShadow.setOffsetY(4);
        innerShadow.setColor(Color.web("0x3b596d"));

        block.setEffect(innerShadow);

        Light.Distant light = new Light.Distant();
        light.setAzimuth(20.0);
        light.setElevation(50.0);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(1.0);

        block.setEffect(lighting);
    }

    //  примитивная проверка на переполнение всех блоков
    public static boolean allBlocksAreOvercrowded() {
        boolean result = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!blocks[i][j].isOvercrowded()) result = false;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        launch(args);
    }
}