package game.model.game.model.worldObject.shape;

import util.Util;
import game.model.game.model.GameModel;
import game.model.game.grid.GridCell;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static util.Const.*;

/**
 * Shouldn't be drawable! Purely for testing for now.
 * Abstract class that defines a generic shape. All entities on a tile
 * have a shape derived from this class.
 */
public abstract class Shape {

    /**
     * Gets the x-coordinate of the center of the shape.
     * @return the x-coordinate of the center of the shape
     */
    public abstract double getX();

    /**
     * Gets the y-coordinate of the center of the shape.
     * @return the y-coordinate of the center of the shape
     */
    public abstract double getY();

    /**
     * Returns true if the shape blocks the cell it is currently on. Returns
     * false otherwise.
     * @param xcell x-coordinate of the upper left corner of the cell
     * @param ycell y-coordinate of the upper left corner of the cell
     * @return true if the shape blocks the cell it is currently on
     */
    public abstract boolean blocksCell(int xcell, int ycell);

    /**
     * Returns true if shape occupies different tiles since the last time this was called
     *
     */
    public abstract boolean moved();

    public static class GridCellReference {

        /**
         * X-coordinate of the upper left corner of the cell.
         */
        public int x;

        /**
         * Y-coordinate of the upper left corner of the cell.
         */
        public int y;

        /**
         * Default constructor.
         */
        GridCellReference() {}

        /**
         * Constructor for the cell reference with given coordinates.
         * @param x x-coordinate of the upper left corner of the cell
         * @param y y-coordinate of the upper left corner of the cell
         */
        GridCellReference(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Constructor for the cell reference with a given cell.
         * @param cell the cell on the game map
         */
        GridCellReference(GridCell cell) {
            this(cell.getTopLeftX(), cell.getTopLeftY());
        }

        /**
         * Gets the cell that this class references.
         * @param model current model of the game
         * @return the cell that is referenced.
         */
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
            return (int) (0.5 * (this.x + this.y) * (this.x + this.y + 1) + this.y);
        }
    }

    /**
     * Return the set of all cells this shape overlaps with. This method
     * should return a new set every time.
     * @param gameMap current model of the game
     * @return the set of all cells that this shape overlaps with.
     */
    public Collection<GridCell> getCells(GameModel gameMap) {
        return getCellsReference().stream().map(a -> a.getCell(gameMap)).collect(Collectors.toSet());
    }

    /**
     * Return the set of all cell references this shape overlaps with.
     * This method should return a new set every time.
     * @return the set of all cell references that this shape overlaps with
     */
    abstract Set<GridCellReference> getCellsReference();

    /**
     * Returns true if this shape collides with the given shape. Returns false
     * otherwise.
     * @param shape the shape to be tested for collisions
     * @return true if this shape collides with the specified shape
     */
    public boolean testCollision(Shape shape) {
        if(Util.setsIntersect(this.getCellsReference(), shape.getCellsReference()))
            return sameCellTestCollision(shape);
        return false;
    }

    /**
     * Returns true if this shape collides with the given shape. It is given
     * that both shapes reside on the same cell. Returns false otherwise.
     * @return true if the two shapes collide
     */
    public abstract boolean sameCellTestCollision(Shape shape);

    /**
     * Shift the shape by the delta specified.
     * @param dx shift in x-coordinate
     * @param dy shift in y-coordinate
     */
    public abstract void shift(double dx, double dy);

    /**
     * Sets the position to the coordinates specified.
     * @param x x-coordinate of the center of the shape
     * @param y y-coordinate of the center of the shape
     */
    public abstract void setPos(double x, double y);

    /**
     * Rotates the shape by the given angle.
     * @param r angle to rotate the shape by
     */
    public abstract void rotate(double r);

    /**
     * Sets the angle of the shape to the angle specified.
     * @param r new angle of the shape.
     */
    public abstract void setAngle(double r);

    /**
     * for debugging hitboxes.
     */
    public abstract void draw(GraphicsContext gc);

    /*
    gets the distance from the center of the shape to the farthest edge at the given angle
     */
    public abstract double getRadius(double angle);

    /*
    the width of the object through its horizontal center line
     */
    public double getWidth() {
        return getRadius(ANGLE_LEFT) + getRadius(ANGLE_RIGHT);
    }

    /*
    the width of the object through its vertical center line
     */
    public double getHeight() {
        return getRadius(ANGLE_UP) + getRadius(ANGLE_DOWN);
    }
}
