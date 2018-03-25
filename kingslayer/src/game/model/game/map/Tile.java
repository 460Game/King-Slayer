package game.model.game.map;

import game.model.game.model.GameModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import util.Util;

import static images.Images.TILE_IMAGE;
import static util.Const.*;
import static util.Util.toDrawCoords;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Enumeration of all possible tiles in the game map. Each tile has an image
 * tied to it as well as a flag that determines whether entities can pass
 * through the tile with nothing on it.
 */
public enum Tile {
    /**
     * Deep water tile.
     */
    DEEP_WATER('W', Color.BLUE),

    /**
     * Grass tile.
     */
    GRASS_0('G', Color.GREENYELLOW),

    /**
     * Grass tile.
     */
    GRASS_1('G', Color.GREENYELLOW),

    /**
     * Grass tile.
     */
    GRASS_2('G', Color.GREENYELLOW),

    /**
     * Generic path tile.
     */
    PATH('D', Color.BEIGE),

    /**
     * Shallow water tile.
     */
    SHALLOW_WATER('B', Color.BROWN),

    UNSET('!', Color.BLACK);

    private Color color;
    public char tupleNum;


    /**
     * Constructor for a tile.
     */
    Tile(char tupleNum, Color color) {
        this.tupleNum = tupleNum;
        this.color = color;
    }

    /**
     * Gets the color of the tile.
     *
     * @return color of the tile
     */
    public Color getColor() {
        return color;
    }
}
