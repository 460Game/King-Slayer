package game.model.Game.Tile;

import javafx.scene.canvas.GraphicsContext;

public abstract class Tile {

    public abstract boolean isPassable();

    public abstract void draw(GraphicsContext gc, int x, int y);
}
