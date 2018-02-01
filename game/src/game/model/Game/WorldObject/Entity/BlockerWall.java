package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

import static Util.Const.TILE_PIXELS;

/**
 * Stationary wall entity in the game world.
 */
public class BlockerWall extends Blocker {

    /**
     * Image of a wall that represents this entity.
     */
    private static Image wall;

    // Gets the image of the box to represent this entity.
    static {
        try {
            wall = new Image(Tile.class.getResource("wall.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {
        gc.drawImage(wall, (this.getX() - 0.5) * TILE_PIXELS,
                (this.getY() - 0.5) * TILE_PIXELS - 2 * wall.getHeight() + 2 * TILE_PIXELS,
                2 * wall.getWidth(), 2 * wall.getHeight());
    }
}
