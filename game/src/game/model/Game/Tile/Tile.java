package game.model.Game.Tile;

import game.model.Drawable;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Tile {

    public abstract boolean isPassable();

    public abstract void draw(GraphicsContext gc, int x, int y);
}
