package Shape;

import Model.GameMap;
import Tile.Tile;
import client.Drawable;

import java.util.Set;

/**
 * shouldnt be drawable! purely for testing for now
 */
public abstract class Shape implements Drawable {

    /**
     * this should return the set of all tiles this shape overlaps with
     * 
     * @param gameMap
     * @return
     */
    public abstract Set<Tile> getTiles(GameMap gameMap);

    public abstract boolean testCollision(Shape shape);

    public abstract void shift(double x, double y);

    public abstract void setPos(double x, double y);
}
