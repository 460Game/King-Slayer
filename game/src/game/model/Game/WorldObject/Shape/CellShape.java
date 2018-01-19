package game.model.Game.WorldObject.Shape;

import javafx.scene.canvas.GraphicsContext;

import java.util.Collections;
import java.util.Set;

/**
 * A shape that always takes up an entire cell, for blocking an entire cell.
 */
public class CellShape extends Shape {

    /**
     * The cell reference that this shape is in.
     */
    private GridCellReference gridCell;

    /**
     * Constructor of the shape with given coordinates.
     * @param x x-coordinate of the upper left corner of the cell
     * @param y y-coordinate of the upper left corner of the cell
     */
    public CellShape(int x, int y) {
        this.gridCell = new GridCellReference(x, y);
    }

    /**
     * Default constructor needed for serialization.
     */
    public CellShape() {

    }

    @Override
    public double getX() {
        return gridCell.x + 0.5;
    }

    @Override
    public double getY() { return gridCell.y + 0.5; }

    @Override
    public boolean blocksCell(int xcell, int ycell) {
        return gridCell.x == xcell && gridCell.y == ycell;
    }

    @Override
    Set<GridCellReference> getCellsReference() {
        return Collections.singleton(gridCell);
    }

    @Override
    public boolean sameCellTestCollision(Shape shape) {
        return true;
    }

    @Override
    public void shift(double x, double y) {
        if ((x - Math.round(x)) > 0.01 || (y - Math.round(y)) > 0.01)
            throw new RuntimeException("Cell shape cannot shift by a non-integer value.");

        this.gridCell.x += Math.round(x);
        this.gridCell.y += Math.round(y);
    }

    @Override
    public void setPos(double x, double y) {
        if ((x - Math.round(x)) > 0.01 || (y - Math.round(y)) > 0.01)
            throw new RuntimeException("Cell shape position cannot be set to a non-integer value.");

        this.gridCell.x = (int) Math.round(x);
        this.gridCell.y = (int) Math.round(y);
    }

    @Override
    public void rotate(double r) {
        throw new RuntimeException("Cell shape cannot rotate.");
    }

    @Override
    public void setAngle(double r) {
        throw new RuntimeException("Cell shape cannot set angle.");
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillRect(this.gridCell.x * 32, this.gridCell.y * 32, 32, 32);
    }
}
