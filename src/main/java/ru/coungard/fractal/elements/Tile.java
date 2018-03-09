package ru.coungard.fractal.elements;

import ru.coungard.fractal.combo.Combo;
import ru.coungard.fractal.main.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


/*  Класс отвечает за сущность(тайл), с которым взаимодействует пользователь, на уровне ячейки.
 После нажатия мышкой по определенной ячейке, происходит смещение в область, которая
  соответствует области внутри блока, в котором находится ячейка. Игра продолжается до тех пор,
   пока 1 из игроков(X or O) не выстраивает ряд из общих блоков по простому принципу крестиков
    ноликов.
 */
public class Tile extends StackPane {
    public static List<Block> completedBlocksX = new ArrayList<>();
    public static List<Block> completedBlocksO = new ArrayList<>();

    public static boolean turnX;
    public static Label turnLabel = new Label();

    private static List<ImageView> views = new ArrayList<>();
    private Text text = new Text();

    public static boolean playable = true;
    public static Tile currentTile;
    public static boolean anchorStep = true; // якорь, которые обрабатывает "лишние" касания

    /*  В конструкторе объявлены прямоугольник, чтобы растянуть тайл, и компонент Text, нужный
     для дальнейшего использования в качестве значения(X или O)
     */
    Tile() {
        Rectangle rect = new Rectangle(60, 60);
        rect.setFill(Color.LAVENDER);
        rect.setStroke(Color.BLACK);
        text.setFont(new Font(40));
        turnLabel.setTextFill(Color.ANTIQUEWHITE);
        turnLabel.setText("Turn of the X");
        turnLabel.setVisible(false);

        getChildren().addAll(rect, text);

        /*  Основной метод класса, обрабатывает нажатие по области нашего тайла. После
         тривиальных действий по обработке ненужных касаний и прочего, выполняется проверка на
          касание правой и левой кнопки мыши (то есть X или O). Очевидным завершением каждого
           варианта является метод ckeckState(), обрабатывающий всевозможные исходы.
         */
        setOnMouseClicked(event -> {
            boolean middleTouch = event.getButton() == MouseButton.MIDDLE;
            if(!playable | middleTouch) {
                anchorStep = false;
                return;
            }

            setCurrentTile(this);
            Main.availability();

            if (event.getButton() == MouseButton.PRIMARY) {
                if(!turnX || !getValue().equals("")){
                    anchorStep = false;
                    return;
                }
                text.setText("X");
                text.setFill(Color.RED);
                turnX = !turnX;
                turnLabel.setText("Turn of the O");
                anchorStep = true;
                checkState();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                if(turnX || !getValue().equals("")){
                    anchorStep = false;
                    return;
                }
                text.setText("O");
                text.setFill(Color.GREEN);
                turnX = !turnX;
                turnLabel.setText("Turn of the X");
                anchorStep = true;
                checkState();
            }
        });
    }

    /* Проверка (которая будет выполнена после каждого хода игроков) на налицие завершенных
     комбинаций внутри каждого блока (3х3). Также осуществляется проверка на налицие комбинаций
      внешних блоков, и как итог, победа одного из игроков. Завершается метод проверкой на
       ничейный результат.
     */
    private void checkState() {
        for (Combo combo : Block.combos) {
            if (combo.isComplete()) {
                placeTheBlock();

                if (Combo.gameOver()) {
                    startWinMessage();
                    playable = false;
                }
                Block.combos.remove(combo);
                break;
            }
        }
        if (Main.allBlocksAreOvercrowded()) {
            startDrawMessage();
            playable = false;
        }
    }

    // Добавляем нужный нам блок в список завершенных блоков X или O. Затем закрашиваем.
    private void placeTheBlock() {

        if (getValue().equals("X") && !completedBlocksO.contains(Main.blockForFill)) {
            completedBlocksX.add(Main.blockForFill);
            fillBlock(Main.blockForFill);

        } else if(getValue().equals("O") && !completedBlocksX.contains(Main.blockForFill)){
            completedBlocksO.add(Main.blockForFill);
            fillBlock(Main.blockForFill);
        }
    }

    /*  Метод накладывает на блок, который мы передаем в аргумент, уже один из заготовленных
     рисунков. X и O в зависимости от блока победителя соответственно.
     */
    private void fillBlock(Block block) {
        String desiredUri = String.format("images/%s.png", getValue());

        Image image = new Image(desiredUri);
        ImageView displayOwnerBlock = new ImageView(image);

        views.add(displayOwnerBlock);

        displayOwnerBlock.setFitWidth(180);
        displayOwnerBlock.setFitHeight(180);
        displayOwnerBlock.setOpacity(0.15);
        displayOwnerBlock.setTranslateX(block.getTranslateX());
        displayOwnerBlock.setTranslateY(block.getTranslateY());
        displayOwnerBlock.isMouseTransparent();
        displayOwnerBlock.setMouseTransparent(true);

        Main.gameRoot.getChildren().add(displayOwnerBlock);
    }

    // устанавливаем тайл, переданный в аргумент, как действующую ячейку
    private static void setCurrentTile(Tile currentTile) {
        Tile.currentTile = currentTile;
    }


    private void startWinMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("game info");
        alert.setContentText("Player " + getValue() + " win!");

        for (ImageView view : views) {
            view.setOpacity(0.8);
        }
        alert.show();
    }

    private void startDrawMessage() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("game info");
        alert.setContentText("DRAW!");

        for (ImageView view : views) {
            view.setOpacity(0.8);
        }
        alert.show();
    }

    // метод возвращает X или O текущего тайла
    public String getValue() {
        return text.getText();
    }
}