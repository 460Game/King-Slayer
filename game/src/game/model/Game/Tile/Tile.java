package game.model.Game.Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import static Util.Const.*;
import java.io.IOException;

public enum Tile {

    BARRIER(false, "barrier.png"),
    DEEP_WATER(false, "deepWater.png"),
    GRASS(true, "grass.png"),
    METAL(true, "metal.png"),
    PATH(true, "rock.png"),
    SHALLOW_WATER(true, "shallowWater.png"),
    STONE(true, "stone.png"),
    TREE(false, "tree.png"),
    WALL(false, "wall.png"),
    FOG(true, "fog.png");

    public boolean IS_PASSIBLE;
    public Image IMAGE;

    Tile(boolean isPassable, String imageName) {
        this.IS_PASSIBLE = isPassable;
        try {
            this.IMAGE = new Image(Tile.class.getResource(imageName).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext gc, int x, int y) {
        gc.drawImage(this.IMAGE, x * TILE_PIXELS, y * TILE_PIXELS, TILE_PIXELS, TILE_PIXELS);
    }
}
