package game.model.Game.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

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
    BARRIER(false, true, "barrier.png", Color.BLACK),

    /**
     * Deep water tile.
     */
    DEEP_WATER(false, false, "deepWater.png", Color.DARKBLUE),

    /**
     * Grass tile.
     */
    GRASS(true, false, "grass.png", Color.GREEN),

    /**
     * TODO
     */
    NO_BUILD(true, false, "nobuild.png", Color.SANDYBROWN),

    /**
     * Generic path tile.
     */
    PATH(true, false, "rock.png", Color.SANDYBROWN),

    /**
     * Shallow water tile.
     */
    SHALLOW_WATER(true, false, "shallowWater.png", Color.LIGHTCYAN),

    /**
     * Wall tile.
     */
    WALL(false, true, "wall.png", Color.BLACK),

    /**
     * Fog tile.
     */
    FOG(true, false, "fog.png", Color.GRAY);

    public final boolean aboveGround;
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
     * Color of the tile.
     */
    private Color color;

    /**
     * Constructor for a tile.
     * @param isPassable flag that says whether a tile can be passed through
     * @param aboveGround flag that says whether the tile can be drawn above the player
     * @param imageName name of the file that holds the tile image
     */
    Tile(boolean isPassable, boolean aboveGround, String imageName, Color color) {
        this.aboveGround = aboveGround;
        this.IS_PASSABLE = isPassable;
        this.color = color;
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

    /**
     * Gets the color of the tile.
     * @return color of the tile
     */
    public Paint getColor() {
        return color;
    }
}
