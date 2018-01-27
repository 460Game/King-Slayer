package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

import static Util.Const.TILE_PIXELS;

public class BlockerBox extends Blocker {

    private static Image wall;

    static {
        try {
            wall = new Image(Tile.class.getResource("barrier.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(wall, (this.getX()-0.5) * TILE_PIXELS, (this.getY()-0.5) * TILE_PIXELS - 2*wall.getHeight() + 2*TILE_PIXELS, 2*wall.getWidth(), 2*wall.getHeight());
    }
}
