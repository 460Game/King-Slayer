package game.model.Game.Map;

import game.model.Game.Model.GameModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import static Util.Const.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
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
    DEEP_WATER(false, false, "deep_water.png", 'W', Color.AQUA),

    /**
     * Grass tile.
     */
    GRASS_0(true, false, "grass.png", 'G', Color.GREENYELLOW),

    /**
     * Grass tile.
     */
    GRASS_1(true, false, "grass_1.png", 'G', Color.GREENYELLOW),

    /**
     * Grass tile.
     */
    GRASS_2(true, false, "grass_2.png", 'G', Color.GREENYELLOW),

    /**
     * Generic path tile.
     */
    PATH(true, false, "crapdirt.png", 'D', Color.BEIGE),

    /**
     * Shallow water tile.
     */
    SHALLOW_WATER(true, false, "shallow_water.png", 'B', Color.BROWN),

    /**
     * Fog tile.
     */
    FOG(true, false, "fog.png", '?', Color.GRAY);

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

    private char tupleNum;

    private static Map<String, Point> tileMap = new HashMap<>();
    private static Map<Character, List<Character>> matches;
    private static int animationTime = 0;
    static {
        Scanner input;
        try {
            input = new Scanner(Tile.class.getResource("tile_map.txt").openStream());

            while (input.hasNext()) {
//                    System.out.println("hello?");
                Point curPoint = new Point(input.nextInt(), input.nextInt());
//                    System.out.println(curPoint.x + " " + curPoint.y);
                if (curPoint.x == -1) break;
                input.nextLine();
                int cur = 0;
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < 3; i++) {
                    str.append(input.nextLine());
                }
                input.nextLine();
                tileMap.put(str.toString(), curPoint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        matches = new HashMap<>();
        matches.put('G', Arrays.asList('G', 'E', 'X', 'L', '_'));
        matches.put('D', Arrays.asList('D', 'E', 'U', 'L', 'K', '_'));
        matches.put('W', Arrays.asList('W', 'J', 'X', 'U', 'L', 'K', '_'));
        matches.put('B', Arrays.asList('B', 'J', 'X', 'U', '_'));
    }


    /**
     * Constructor for a tile.
     * @param isPassable flag that says whether a tile can be passed through
     * @param aboveGround flag that says whether the tile can be drawn above the player
     * @param imageName name of the file that holds the tile image
     */
    Tile(boolean isPassable, boolean aboveGround, String imageName, char tupleNum, Color color) {
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
        StringBuilder hashKey = new StringBuilder();
        for (int j = -1; j < 2; j++) {
            for (int i = -1; i < 2; i++) {
                hashKey.append(model.getTile(x + i, y + j).tupleNum);
            }
        }
        //System.out.println(hashKey);
        try {
            int max = -1;
            Point maxPoint = new Point(0, 0);
            for(String key: tileMap.keySet()) {
                if (key.charAt(4) != hashKey.charAt(4)) // check that middle tile matches
                    continue;
                int cur = 0;
                Point curPoint = tileMap.get(key);
                for (int l = 0; l < 9; l++) {
                    if (matches.get(hashKey.charAt(l)).contains(key.charAt(l))) // check that these two are a possible match
                        cur++;
                }
                if (cur > max) {
                    max = cur;
                    maxPoint = curPoint;
                }
            }
            if (this.tupleNum == 'W' || this.tupleNum == 'w') {
                if (animationTime < 40) {
                    gc.drawImage(this.IMAGE,
                        maxPoint.x * 32, maxPoint.y * 32, 32, 32,
                        x * TILE_PIXELS, y * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS, 64, 64);
                } else {
                    gc.drawImage(this.IMAGE,
                        (maxPoint.x + 10) * 32, maxPoint.y * 32, 32, 32,
                        x * TILE_PIXELS, y * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS, 64, 64);
                }
            } else {
                gc.drawImage(this.IMAGE,
                    maxPoint.x * 32, maxPoint.y * 32, 32, 32,
                    x * TILE_PIXELS, y * TILE_PIXELS - 2 * 32 + 2 * TILE_PIXELS, 64, 64);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*Tile n = model.getTile(x, y - 1);
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
        }*/
    }

    /**
     * Gets the color of the tile.
     * @return color of the tile
     */
    public Paint getColor() {
        return color;
    }
}
