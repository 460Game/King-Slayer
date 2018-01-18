package game.model.Game.WorldObject.Shape;

import Util.Util;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Set;
import static Util.Const.*;

public class CircleShape extends Shape {

    private double x, y, radius;

    transient Set<GridCellReference> memo = null;

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public boolean blocksCell(int x, int y) {
        return false;
        // TODO actual math/corners
    }

    @Override
    Set<GridCellReference> getCellsReference() {
        if(memo != null )
            return memo;
        memo = new HashSet<>();

        for(int i = (int) (x - radius + 0.5); i <= Math.ceil(x + radius + 0.5); i++)
            for(int j = (int)(y - radius + 0.5); j <= Math.ceil(y + radius + 0.5); j++)
                if(Util.dist(this.x, this.y, i + 0.5, j + 0.5) <= radius + 0.70710678118)
                    memo.add(new GridCellReference(i,j));

        return memo;
    }

    @Override
    public boolean sameCellTestCollision(Shape shape) {
        if(shape instanceof CircleShape) {
            CircleShape circle = (CircleShape) shape;
            return Util.dist(this.x, this.y, circle.x , circle.y) <= this.radius + circle.radius;
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

        throw new RuntimeException("Dont know how to collide circle with this other thing");
    }

    @Override
    public void shift(double x, double y) {
        memo = null;
        this.x += x;
        this.y += y;
    }

    @Override
    public void setPos(double x, double y) {
        memo = null;
        this.x = x;
        this.y = y;
    }

    @Override
    public void rotate(double r) {
        //Do nothing - yay for circles
    }

    @Override
    public void setAngle(double r) {
        //Do nothing - yay for circles
    }

    public CircleShape(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x*TILE_PIXELS,y*TILE_PIXELS,TILE_PIXELS * radius,TILE_PIXELS * radius);
    }
}
