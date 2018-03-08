package ru.coungard.fractal.elements;

import ru.coungard.fractal.combo.Combo;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class Block extends StackPane {

   static List<Combo> combos = new ArrayList<>();
    public Tile[][] board = new Tile[3][3];

    public Block() {

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 60);
                tile.setTranslateY(i * 60);

                getChildren().add(tile);
                board[j][i] = tile;
            }
        }

        createCombos();
    }

    private void createCombos() {
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }

        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));
    }

    public boolean isOvercrowded() {
        boolean result = true;
        for (int i = 0; i <3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[j][i].getValue().equals("")) result = false;
            }
        }

        return result;
    }
}
