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
    DEEP_WATER('W', Color.AQUA),

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

    /**
     * Fog tile.
     */
    FOG('?', Color.GRAY);

    private Color color;
    private char tupleNum;

    private static Map<String, Point> TILE_MAP;
    private static Map<Character, List<Character>> matches;
    private static final int TILE_IMAGE_TILE_SIZE = 32;

    static {

        TILE_MAP = new HashMap<>();
        matches = new HashMap<>();

        Scanner input = new Scanner(Tile.class.getResourceAsStream("tile_map.txt"));

        while (input.hasNext()) {
            Point p = new Point(input.nextInt(), input.nextInt());
            if (p.x == -1)
                break;
            input.nextLine();
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < 3; i++)
                str.append(input.nextLine());
            input.nextLine();
            TILE_MAP.put(str.toString(), p);
        }

        matches.put('G', Arrays.asList('G', 'E', 'X', 'L', 'K', '_'));
        matches.put('D', Arrays.asList('D', 'E', 'U', 'L', 'K', '_'));
        matches.put('W', Arrays.asList('W', 'J', 'X', 'U', 'L', '_'));
        matches.put('B', Arrays.asList('B', 'J', 'X', 'U', 'E', '_'));
    }


    /**
     * Constructor for a tile.
     */
    Tile(char tupleNum, Color color) {
        this.tupleNum = tupleNum;
        this.color = color;
    }

    /**
     * Draws the tile in a specified cell on the map.
     *
     * @param gc context used to draw the tile
     * @param x  x-coordinate of the upper left corner of the cell holding this
     *           tile
     * @param y  y-coordinate of the upper left corner of the cell holding this
     *           tile
     */
    public void draw(GraphicsContext gc, int x, int y, GameModel model, boolean firstAnimation) {

        StringBuilder hashKey = new StringBuilder();

        for (int j = -1; j < 2; j++)
            for (int i = -1; i < 2; i++)
                hashKey.append(model.getTile(x + i, y + j).tupleNum);

        int max = -1;
        Point maxPoint = new Point(0, 0);
        for (String key : TILE_MAP.keySet()) {

            if (key.charAt(4) != hashKey.charAt(4)) // check that middle tile matches
                continue;

            int cur = 0;
            Point curPoint = TILE_MAP.get(key);

            for (int l = 0; l < 9; l++)
                if (matches.get(hashKey.charAt(l)).contains(key.charAt(l))) // check that these two are a possible match
                    cur++;

            if (cur > max) {
                max = cur;
                maxPoint = curPoint;
            }
        }

        if (!firstAnimation && this.tupleNum == 'W')
            gc.drawImage(TILE_IMAGE,
                (maxPoint.x + 10) * TILE_IMAGE_TILE_SIZE,
                maxPoint.y * TILE_IMAGE_TILE_SIZE, TILE_IMAGE_TILE_SIZE, TILE_IMAGE_TILE_SIZE,
                toDrawCoords(x),
                toDrawCoords(y),
                toDrawCoords(1),
                toDrawCoords(1));
        else
            gc.drawImage(TILE_IMAGE,
                maxPoint.x * TILE_IMAGE_TILE_SIZE, maxPoint.y * TILE_IMAGE_TILE_SIZE, TILE_IMAGE_TILE_SIZE, TILE_IMAGE_TILE_SIZE,
                x * TILE_PIXELS, y * TILE_PIXELS - 2 * TILE_IMAGE_TILE_SIZE + 2 * TILE_PIXELS, TILE_PIXELS, TILE_PIXELS);

    }

    /**
     * Gets the color of the tile.
     *
     * @return color of the tile
     */
    public Paint getColor() {
        return color;
    }
}
