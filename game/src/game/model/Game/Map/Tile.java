package game.model.Game.Map;

import game.model.Game.Model.GameModel;
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
     * Deep water tile.
     */
    DEEP_WATER(false, false, "deep_water.png", FourTuple.WATER, Color.DARKBLUE),

    /**
     * Grass tile.
     */
    GRASS_0(true, false, "grass.png", FourTuple.GRASS, Color.GREENYELLOW),

    /**
     * Grass tile.
     */
    GRASS_1(true, false, "grass_1.png", FourTuple.GRASS, Color.GREENYELLOW),

    /**
     * Grass tile.
     */
    GRASS_2(true, false, "grass_2.png", FourTuple.GRASS, Color.GREENYELLOW),

    /**
     * Generic path tile.
     */
    PATH(true, false, "crapdirt.png", FourTuple.DIRT, Color.BEIGE),

    /**
     * Shallow water tile.
     */
    SHALLOW_WATER(true, false, "shallow_water.png", FourTuple.WATER, Color.AQUA),

    /**
     * Fog tile.
     */
    FOG(true, false, "fog.png", 10, Color.GRAY);

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

    private int tupleNum;

    /**
     * Constructor for a tile.
     * @param isPassable flag that says whether a tile can be passed through
     * @param aboveGround flag that says whether the tile can be drawn above the player
     * @param imageName name of the file that holds the tile image
     */
    Tile(boolean isPassable, boolean aboveGround, String imageName, int tupleNum, Color color) {
        this.aboveGround = aboveGround;
        this.IS_PASSABLE = isPassable;
        this.tupleNum = tupleNum;
        try {
            this.IMAGE = new Image(Tile.class.getResource("tile_map.png").openStream());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            System.err.println("Get null: " + imageName);
        }
        this.color = color;//IMAGE.getPixelReader().getColor(0, 0);
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
            this.IMAGE = new Image(Tile.class.getResource("tile_map.png").openStream());
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
    public void draw(GraphicsContext gc, int x, int y, GameModel model) {
        Tile n = model.getTile(x, y - 1);
        Tile e = model.getTile(x + 1, y);
        Tile s = model.getTile(x, y + 1);
        Tile w = model.getTile(x - 1, y);
        int hashKey = FourTuple.hashToFourTuple(this.tupleNum, n.tupleNum, e.tupleNum, s.tupleNum, w.tupleNum);
        try {
            gc.drawImage(this.IMAGE, FourTuple.map.get(hashKey).x, FourTuple.map.get(hashKey).y, 32, 32,
                x * TILE_PIXELS, y * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS, 64, 64);
        } catch (Exception error) {
            if (tupleNum == FourTuple.GRASS) {
                gc.drawImage(this.IMAGE,
                    32, 64, 32, 32,
                    x * TILE_PIXELS,y * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS,64,64);
            } else if (tupleNum == FourTuple.DIRT) {
                gc.drawImage(this.IMAGE,
                    128, 192, 32, 32,
                    x * TILE_PIXELS,y * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS,64,64);
            } else if (tupleNum == FourTuple.WATER && this.IS_PASSABLE) {
                gc.drawImage(this.IMAGE,
                    96, 672, 32, 32,
                    x * TILE_PIXELS,y * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS,64,64);
            } else if (tupleNum == FourTuple.WATER && !this.IS_PASSABLE) {
                gc.drawImage(this.IMAGE,
                    160, 672, 32, 32,
                    x * TILE_PIXELS,y * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS,64,64);
            } else {
                error.printStackTrace();
            }
        }
    }

    /**
     * Gets the color of the tile.
     * @return color of the tile
     */
    public Paint getColor() {
        return color;
    }
}
