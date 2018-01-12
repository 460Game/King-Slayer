package Tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileMetal extends TileTest {

    public TileMetal(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public Paint getColor() {
        return Color.SKYBLUE;
    }
}
