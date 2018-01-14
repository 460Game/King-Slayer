package game.model.Game.Tile;

import javafx.scene.canvas.GraphicsContext;

public class unknownTile extends Tile {

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public void draw(GraphicsContext gc, int x, int y) {

    }
}
