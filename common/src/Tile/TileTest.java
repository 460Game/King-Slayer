package Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;

public abstract class TileTest extends Tile {

    @Override
    public boolean isPassable() {
        return true;
    }

    double x,y,w,h;

    public TileTest(int x, int y) {
        super(x, y);
        this.x = x*10;
        this.y = y*10;
        this.w = 10;
        this.h = 10;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(getColor());
        gc.fillRect(x,y,w,h);
        gc.strokeRect(x,y,w,h);
    }

    public abstract Paint getColor();

}
