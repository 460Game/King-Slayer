package Tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;


/*
demo tile
 */
public class TileWater extends TileTest {

    @Override
    public boolean isPassable() {
        return false;
    }

    public TileWater(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.BLUE;
    }
}
