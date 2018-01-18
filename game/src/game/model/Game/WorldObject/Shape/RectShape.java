package game.model.Game.WorldObject.Shape;

import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Set;
import static Util.Const.*;

public class RectShape extends Shape {

    //x,y are center of the rectangle
    //w,h are the width and height
    //radius is the rotation in radians
    private double x, y, w, h, r;

    Set<GridCellReference> memo = null;

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
        return false; //RYAN TODO
    }

    @Override
    Set<GridCellReference> getCellsReference() {
        if (memo != null)
            return memo;

        memo = new HashSet<>();

        //TODO make this factor in rotation

        for (int i = (int) (x - w); i <= Math.ceil(x + w); i++)
            for (int j = (int) (y - h); j <= Math.ceil(y + h); j++)
                memo.add(new GridCellReference(i, j));

        return memo;
    }

    @Override
    public boolean sameCellTestCollision(Shape shape) {

        //TODO Not implemented yet

        if (shape instanceof CircleShape) {
            //circle cricle
            CircleShape circle = (CircleShape) shape;
            return false; //TODO this
            // return Util.Util.dist(this.x, this.y, circle.x , circle.y) <= this.radius + circle.radius;
        } else if (shape instanceof RectShape) {
            //rectangle rectangle
            RectShape rect = (RectShape) shape;
            //cirlce rectange collisions are such a pain!
            return false; //TODO!!
        } else if (shape instanceof CompositeShape) {
            CompositeShape compositeShape = (CompositeShape) shape;
            return compositeShape.testCollision(this);
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
        memo = null;
        this.r += r;
    }

    @Override
    public void setAngle(double r) {
        memo = null;
        this.r = r;
    }

    RectShape(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = 0;
    }

    RectShape(double x, double y, double w, double h, double r) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.r = r;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillRect(x * TILE_PIXELS, y * TILE_PIXELS, w * TILE_PIXELS, h * TILE_PIXELS);
    }
}
