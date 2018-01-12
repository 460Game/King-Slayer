package Tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileTree extends TileTest {

    public TileTree(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.GREEN;
    }
}
