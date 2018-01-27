package game.model.Game.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import static Util.Const.*;
import java.io.IOException;
import java.util.Random;

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
    DEEP_WATER(false, false, "deep_water.png", Color.DARKBLUE),

    /**
     * Grass tile.
     */
    GRASS_0(true, false, "grass.png"),

    /**
     * Grass tile.
     */
    GRASS_1(true, false, "grass_1.png"),

    /**
     * Grass tile.
     */
    GRASS_2(true, false, "grass_2.png"),

    /**
     * TODO
     */
    NO_BUILD(true, false, "nobuild.png", Color.LIGHTGREY),

    /**
     * Generic path tile.
     */
    PATH(true, false, "crapdirt.png", Color.LIGHTGREY),

    /**
     * Shallow water tile.
     */
    SHALLOW_WATER(true, false, "shallow_water.png", Color.LIGHTCYAN),

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
        this.color = IMAGE.getPixelReader().getColor(0, 0);
    }

    /**
     * Constructor for a tile.
     * @param isPassable flag that says whether a tile can be passed through
     * @param aboveGround flag that says whether the tile can be drawn above the player
     * @param imageName name of the file that holds the tile image
     */
    Tile(boolean isPassable, boolean aboveGround, String imageName) {
        this.aboveGround = aboveGround;
        this.IS_PASSABLE = isPassable;
        try {
            this.IMAGE = new Image(Tile.class.getResource(imageName).openStream());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.err.println("Get null: " + imageName);
        }
        this.color = IMAGE.getPixelReader().getColor(0, 0);
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
        gc.drawImage(this.IMAGE, x * TILE_PIXELS, y * TILE_PIXELS - 2*this.IMAGE.getHeight() + 2*TILE_PIXELS, 2*this.IMAGE.getWidth(), 2*this.IMAGE.getHeight());
    }

    /**
     * Gets the color of the tile.
     * @return color of the tile
     */
    public Paint getColor() {
        return color;
    }
}
