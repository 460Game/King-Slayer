package Util;

/**
 * Abstract class that defines various constants to be used.
 */
public abstract class Const {

    /**
     * Width of the game map, in terms of number of grid cells.
     */
    public final static int GRID_X_SIZE = 100;

    /**
     * Height of the game map, in terms of number of grid cells.
     */
    public final static int GRID_Y_SIZE = 100;

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

    /**
     * Number of pixels on the width of the canvas.
     */
    public final static int CANVAS_WIDTH = GRID_X_SIZE * TILE_PIXELS;

    /**
     * Number of pixels on the height of the canvas.
     */
    public final static int CANVAS_HEIGHT = GRID_Y_SIZE * TILE_PIXELS;

    /**
     * Number of updates per second
     * should match with 1e9/UPDATE_LOOP_TIME_NANOS = UPDATE_LOOP_TIME_NANOS
     */
    public final static int UPDATES_PER_SECOND = 60;

    /**
     * Number of nanoseconds to optimaly spend on each update loop
     */
    public final static int UPDATE_LOOP_TIME_NANOS = 16666667;
}
