package game.model.Game.WorldObject.Shape;

import game.model.Game.GameModel;
import game.model.Game.Grid.GridCell;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * shouldnt be drawable! purely for testing for now
 */
public abstract class Shape {

    public abstract double getX();
    public abstract double getY();

    static class GridCellReference {
        public int x, y;

        GridCellReference(int x, int y) {
            this.x = x;
            this.y = y;
        }

        GridCellReference(GridCell cell) {
            this.x = cell.x;
            this.y = cell.y;
        }

        public GridCell getCell(GameModel model) {
            return model.getCell(x, y);
        }

    }

    /**
     * this should return the set of all tiles this shape overlaps with
     * returns a new set every time
     * @param gameMap
     * @return
     */
    public Collection<GridCell> getCells(GameModel gameMap) {
        return getCellsReference().stream().map(a -> a.getCell(gameMap)).collect(Collectors.toSet());
    }

    abstract Set<GridCellReference> getCellsReference();

    /**
     * given that they are on same cell, do they collide?
     * @param shape
     * @return
     */
    public abstract boolean testCollision(Shape shape);

    /**
     * shift the shape by the delta
     * @param dx
     * @param dy
     */
    public abstract void shift(double dx, double dy);

    public abstract void setPos(double x, double y);

    public abstract void rotate(double r);

    public abstract void setAngle(double r);

    /**
     * temp for debugging
     */
    public abstract void draw(GraphicsContext gc);
}
