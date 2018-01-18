package Util;

import game.model.Game.GameModel;

/**
 * Abstract class that defines various constants to be used.
 */
public abstract class Const {

    /**
     * Width of the initial game screen.
     */
    public final static int INIT_SCREEN_WIDTH = 800;

    /**
     * Height of the initial game screen.
     */
    public final static int INIT_SCREEN_HEIGHT = 600;

    /**
     * Number of pixels on the side of a tile.
     */
    public final static int TILE_PIXELS = 32;

    // TODO comment
    public final static int CANVAS_WIDTH = GameModel.GRID_X_SIZE * TILE_PIXELS;
    public final static int CANVAS_HEIGHT = GameModel.GRID_Y_SIZE * TILE_PIXELS;
}
