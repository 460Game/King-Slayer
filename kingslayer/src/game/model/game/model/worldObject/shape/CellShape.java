package game.model.game.model.worldObject.shape;

import javafx.scene.canvas.GraphicsContext;
import util.Util;

import java.util.Collections;
import java.util.Set;

import static util.Util.toDrawCoords;

/**
 * A shape that always takes up an entire cell, for entities that
 * block an entire cell.
 */
public class CellShape extends Shape {

    /**
     * The cell reference that this shape is in.
     */
    private GridCellReference gridCell;

    /**
     * Flag that determines whether this shape has moved.
     */
    private boolean moved = true;

    /**
     * Default constructor needed for serialization.
     */
    public CellShape() {
        gridCell = new GridCellReference(0, 0);
    }

    /**
     * Constructor of the shape with given coordinates.
     * @param x x-coordinate of the upper left corner of the cell
     * @param y y-coordinate of the upper left corner of the cell
     */
    public CellShape(int x, int y) {
        gridCell = new GridCellReference(x, y);
    }

    @Override
    public double getX() { return gridCell.x + 0.5; }

    @Override
    public double getY() { return gridCell.y + 0.5; }

    @Override
    public boolean blocksCell(int xcell, int ycell) {
        return gridCell.x == xcell && gridCell.y == ycell;
    }

    @Override
    public boolean moved() {
        if(moved) {
            moved = false;
            return true;
        }
        return false;
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
        if ( !Util.closeDouble(x, Math.round(x)) || !Util.closeDouble(y, Math.round(y)))
            throw new RuntimeException("Cell shape cannot shift by a non-integer value.");
        moved = true;
        this.gridCell.x += Math.round(x);
        this.gridCell.y += Math.round(y);
    }

    @Override
    public void setPos(double x, double y) {
        x -= 0.5; // Translate from center to grid coordinates.
        y -= 0.5;
        moved = true;
        this.gridCell.x = (int) Math.round(x);
        this.gridCell.y = (int) Math.round(y);
        if ( !Util.closeDouble(x, this.gridCell.x) || !Util.closeDouble(y, this.gridCell.y))
            throw new RuntimeException("Cell shape position cannot be set to a non-integer value.");
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
        gc.fillRect(toDrawCoords(getX()), toDrawCoords(getY()), toDrawCoords(getWidth()), toDrawCoords(getHeight()));
    }

    @Override
    public double getRadius(double angle) {
        throw new RuntimeException("NOt implemented yet");
    }
}
