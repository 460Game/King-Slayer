package game.model.Game.WorldObject.Shape;

import Util.Util;
import game.model.Game.GameModel;
import game.model.Game.Grid.GridCell;
import game.model.Game.WorldObject.Entity;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * shouldnt be drawable! purely for testing for now
 */
public abstract class Shape {

    /**
     * Gets the center of the shape
     * @return
     */
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

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null)
                return false;
            if (getClass() != o.getClass())
                return false;
            GridCellReference ref = (GridCellReference) o;
            return this.x == ref.x && this.y == ref.y;
        }

        @Override
        public int hashCode() {
            return (int) (0.5*(this.x + this.y)*(this.x + this.y + 1) + this.y);
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
     * do they collide?
     * @param shape
     * @return
     */
    public boolean testCollision(Shape shape) {
        if(Util.setsIntersect(this.getCellsReference(), shape.getCellsReference()))
            return sameCellTestCollision(shape);
        return false;
    }

    /*
    given they are on the same cell, do they collide?
     */
    public abstract boolean sameCellTestCollision(Shape shape);

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
