package util;

import static java.lang.Math.PI;

/**
 * Abstract class that defines various constants to be used.
 */
public abstract class Const {

    public static final boolean USE_SHAPE_DRAW = true;

    public static final double ANGLE_RIGHT = 0;
    public static final double ANGLE_LEFT = PI;
    public static final double ANGLE_DOWN = PI/2;
    public static final double ANGLE_UP = -PI/2;
    public static final double ANGLE_UP_RIGHT = -PI/4;
    public static final double ANGLE_UP_LEFT = 3*PI/4;
    public static final double ANGLE_DOWN_RIGHT = PI/4;
    public static final double ANGLE_DOWN_LEFT = 3*PI/4;

    public static final double NANOS_TO_SECONDS = 1e-9;
    private static final double SECONDS_TO_NANOS = 1e9;

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
    public final static int TILE_PIXELS = 64;

    /**
     * Number of pixels on the width of the canvas.
     */
    public final static int CANVAS_WIDTH = GRID_X_SIZE * TILE_PIXELS;

    /**
     * Number of pixels on the height of the canvas.
     */
    public final static int CANVAS_HEIGHT = GRID_Y_SIZE * TILE_PIXELS;

    /**
     * Number of updates per second.
     * Should equal 1e9/UPDATE_LOOP_TIME_NANOS = UPDATE_LOOP_TIME_NANOS.
     */
    public final static int UPDATES_PER_SECOND = 60;

    /**
     * Number of nanoseconds to optimally spend on each update loop.
     */
    public final static int UPDATE_LOOP_TIME_NANOS = 16666667;
}
