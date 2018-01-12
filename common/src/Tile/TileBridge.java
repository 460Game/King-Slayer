package Tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileBridge extends TileTest {

    public TileBridge(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.CYAN;
    }
}
