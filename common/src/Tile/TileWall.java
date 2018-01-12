package Tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileWall extends TileTest {

    @Override
    public boolean isPassable() {
        return false;
    }

    public TileWall(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.BLACK;
    }
}
