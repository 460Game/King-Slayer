package Tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileStone extends TileTest {

    public TileStone(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.BLUEVIOLET;
    }
}
