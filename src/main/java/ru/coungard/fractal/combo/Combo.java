package ru.coungard.fractal.combo;

import ru.coungard.fractal.elements.Block;
import ru.coungard.fractal.elements.Tile;
import ru.coungard.fractal.main.Main;

public class Combo {

    private Tile[] tiles;

    public Combo(Tile... tiles) {
        this.tiles = tiles;
    }

    public boolean isComplete() {
        return (!tiles[0].getValue().isEmpty()
                && tiles[0].getValue().equals(tiles[1].getValue())
                && tiles[0].getValue().equals(tiles[2].getValue()));
    }

    public static boolean gameOver() {
        for (int i = 0; i < 3; i++) {
            if ((containsX(Main.blocks[i][0]) && containsX(Main.blocks[i][1]) && containsX(Main.blocks[i][2]))
                || (containsO(Main.blocks[i][0]) && containsO(Main.blocks[i][1]) && containsO(Main.blocks[i][2]))) return true;
        }

        for (int j = 0; j < 3; j++) {
            if ((containsX(Main.blocks[0][j]) && containsX(Main.blocks[1][j]) && containsX(Main.blocks[2][j]))
                || (containsO(Main.blocks[0][j]) && containsO(Main.blocks[1][j]) && containsO(Main.blocks[2][j]))) return true;
        }

        if ((containsX(Main.blocks[0][0]) && containsX(Main.blocks[1][1]) && containsX(Main.blocks[2][2]))
            || (containsO(Main.blocks[0][0]) && containsO(Main.blocks[1][1]) && containsO(Main.blocks[2][2]))) return true;

        if ((containsX(Main.blocks[2][0]) && containsX(Main.blocks[1][1]) && containsX(Main.blocks[0][2]))
            || (containsO(Main.blocks[2][0]) && containsO(Main.blocks[1][1]) && containsO(Main.blocks[0][2]))) return true;

        return false;
    }

    static private boolean containsX(Block block) {
        return Tile.completedBlocksX.contains(block);
    }

    static private boolean containsO(Block block) {
        return Tile.completedBlocksO.contains(block);
    }


}
