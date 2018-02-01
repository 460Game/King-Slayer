package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

import static Util.Const.TILE_PIXELS;

/**
 * Stationary box entity in the game world.
 */
public class BlockerBox extends Blocker {

    /**
     * Image of a box that represents this entity.
     */
    private static Image box;

    // Gets the image of the box to represent this entity.
    static {
        try {
            box = new Image(Tile.class.getResource("barrier.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {
        gc.drawImage(box, (this.getX() - 0.5) * TILE_PIXELS,
                (this.getY() - 0.5) * TILE_PIXELS - 2 * box.getHeight() + 2 * TILE_PIXELS,
                2 * box.getWidth(), 2 * box.getHeight());
    }
}
