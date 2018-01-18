package game.model.Game.Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import static Util.Const.*;
import java.io.IOException;

/**
 * Enumeration of all possible tiles in the game map. Each tile has an image
 * tied to it as well as a flag that determines whether entities can pass
 * through the tile with nothing on it.
 */
public enum Tile {

    /**
     * Barrier tile.
     */
    BARRIER(false, "barrier.png"),

    /**
     * Deep water tile.
     */
    DEEP_WATER(false, "deepWater.png"),

    /**
     * Grass tile.
     */
    GRASS(true, "grass.png"),

    /**
     * TODO
     */
    NO_BUILD(true, "nobuild.png"),

    /**
     * Tile with metal.
     */
    METAL(true, "metal.png"),

    /**
     * Generic path tile.
     */
    PATH(true, "rock.png"),

    /**
     * Shallow water tile.
     */
    SHALLOW_WATER(true, "shallowWater.png"),

    /**
     * Tile with stone.
     */
    STONE(true, "stone.png"),

    /**
     * Tile with a tree (wood).
     */
    TREE(false, "tree.png"),

    /**
     * Wall tile.
     */
    WALL(false, "wall.png"),

    /**
     * Fog tile.
     */
    FOG(true, "fog.png");

    /**
     * Flag that determines whether entities can pass through the specified
     * tile if nothing is on it.
     */
    public boolean IS_PASSABLE;

    /**
     * Image used to render the tile on the map.
     */
    public Image IMAGE;

    /**
     * Constructor for a tile.
     * @param isPassable flag that says whether a tile can be passed through
     * @param imageName name of the file that holds the tile image
     */
    Tile(boolean isPassable, String imageName) {
        this.IS_PASSABLE = isPassable;
        try {
            this.IMAGE = new Image(Tile.class.getResource(imageName).openStream());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.err.println("Get null: " + imageName);
        }
    }

    /**
     * Draws the tile in a specified cell on the map.
     * @param gc context used to draw the tile
     * @param x x-coordinate of the upper left corner of the cell holding this
     *          tile
     * @param y y-coordinate of the upper left corner of the cell holding this
     *          tile
     */
    public void draw(GraphicsContext gc, int x, int y) {
        gc.drawImage(this.IMAGE, x * TILE_PIXELS, y * TILE_PIXELS, TILE_PIXELS, TILE_PIXELS);
    }
}
