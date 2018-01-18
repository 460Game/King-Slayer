package Util;

import game.model.Game.GameModel;

public abstract class Const {




    public final static int INIT_SCREEN_WIDTH = 800;
    public final static int INIT_SCREEN_HEIGHT = 600;

    public final static int TILE_PIXELS = 32;

    public final static int CANVAS_WIDTH = GameModel.GRID_X_SIZE * TILE_PIXELS;
    public final static int CANVAS_HEIGHT = GameModel.GRID_Y_SIZE * TILE_PIXELS;
}
