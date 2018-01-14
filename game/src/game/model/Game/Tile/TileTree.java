package game.model.Game.Tile;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileTree extends TileTest {

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public Paint getColor() {
        return Color.GREEN;
    }
}
