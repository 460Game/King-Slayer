package game.model.Game.Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class TileTest extends Tile {

    @Override
    public boolean isPassable() {
        return true;
    }

    @Override
    public void draw(GraphicsContext gc, int x, int y) {
        gc.setFill(getColor());
        gc.fillRect(x*64,y*64,64,64);
     //   gc.strokeRect(x*10,y*10,10,10);
    }

    public abstract Paint getColor();

}
