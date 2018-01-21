package game.model.Game.WorldObject.Shape;

import Util.Util;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;
import static Util.Const.*;

/**
 * A shape that is a circle.
 */
public class CircleShape extends Shape {

    /**
     * X-coordinate of the center of the circular shape.
     */
    private double x;

    /**
     * Y-coordinate of the center of the circular shape.
     */
    private double y;

    /**
     * Radius of the circular shape.
     */
    private double radius;

    /**
     * Set of grid cell references that this shape is currently on.
     */
    transient Set<GridCellReference> memo = null;

    /**
     * Constructor of a circle shape with given coordinates and radius.
     * @param x x-coordinate of the center of the circle
     * @param y y-coordinate of the center of the circle
     * @param radius radius of the circle
     */
    public CircleShape(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    /**
     * Default constructor needed for serialization.
     */
    public CircleShape() {

    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    public double getRadius() { return radius; }

    @Override
    public boolean blocksCell(int xcell, int ycell) {
        // Get other coordinates of the cell.
        int rightx = xcell + 1;
        int bottomy = ycell + 1;

        // Get the coordinates of the farthest corner.
        double maxx = Math.max(Math.abs(1.0 * rightx - x), Math.abs(1.0 * xcell - x));
        double maxy = Math.max(Math.abs(1.0 * bottomy - y), Math.abs(1.0 * ycell -y ));

        // Check distance to see if farthest corner is in the circle.
        return Util.dist(maxx, maxy, x, y) <= radius;
        // TODO better definition?
    }

    boolean moved = true;

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
        if(memo != null)
            return memo;

        memo = new HashSet<>();

        // Look at all close cells to the circle and check if the circle
        // overlaps with them.
        for(int i = (int) (x - radius); i <= Math.ceil(x + radius); i++)
            for(int j = (int) (y - radius); j <= Math.ceil(y + radius); j++)
                if(Util.dist(this.x, this.y, i + 0.5, j + 0.5) <= radius + 0.70710678118)
                    memo.add(new GridCellReference(i, j));

        return memo;
    }

    @Override
    public boolean sameCellTestCollision(Shape shape) {
        if(shape instanceof CircleShape) {
            CircleShape circle = (CircleShape) shape;
            return Util.dist(x, y,  circle.getX() , circle.getY()) <= this.radius + circle.radius;
        } else if(shape instanceof RectShape) {
            RectShape rect = (RectShape) shape;
            return rect.testCollision(this);
        } else if(shape instanceof CompositeShape) {
            CompositeShape compositeShape = (CompositeShape) shape;
            return compositeShape.testCollision(this);
        } else if(shape instanceof CellShape) {
            CellShape cellShape = (CellShape) shape;
            return cellShape.testCollision(this);
        }

        throw new RuntimeException("Don't know how to collide circle with this other thing");
    }

    @Override
    public void shift(double x, double y) {
        memo = null; // Need to recalculate cells shape is on later.
        moved = true;
        this.x += x;
        this.y += y;
    }

    @Override
    public void setPos(double x, double y) {
        memo = null; // Need to recalculate cells shape is on later.
        moved = true;
        this.x = x;
        this.y = y;
    }

    /**
     * Rotates the shape by the given angle. This doesn't change the center
     * or radius, so nothing happens.
     * @param r angle to rotate the shape by
     */
    @Override
    public void rotate(double r) {
        //Do nothing - yay for circles
    }

    /**
     * Sets the angle of the shape to the angle specified. This doesn't
     * change the center or radius, so nothing happens.
     * @param r new angle of the shape.
     */
    @Override
    public void setAngle(double r) {
        //Do nothing - yay for circles
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillOval(x * TILE_PIXELS - TILE_PIXELS * radius * 0.5, y * TILE_PIXELS - TILE_PIXELS * radius * 0.5, TILE_PIXELS * radius, TILE_PIXELS * radius);
    }
}
