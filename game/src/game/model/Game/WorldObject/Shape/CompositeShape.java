package game.model.Game.WorldObject.Shape;

import Util.Util;
import javafx.scene.canvas.GraphicsContext;

import java.util.Set;

/**
 * TODO the logic in here is a pain because of handling rotation
 * will leave uncompleted until we need it :)
 */
public class CompositeShape extends Shape {
    //todo change to be more then composition of just 2
    private Shape a;
    private double offX;
    private double offY; // shape a is -offX/2, -offY/2 offset from center, shape b is offX/2 offY/2 from center
    private Shape b;

    @Override
    public double getX() {
        return (a.getX() + b.getX());
    }

    @Override
    public double getY() {
        return (a.getY() + b.getY());
    }

    @Override
    Set<GridCellReference> getCellsReference() {
        Set<GridCellReference> set = a.getCellsReference();
        set.addAll(b.getCellsReference());
        return set;
    }

    @Override
    public boolean sameCellTestCollision(Shape shape) {
        //slightly ugly - because needs to ensure the garentee that cells overlapp for the subshapes as well
        return (Util.setsIntersect(shape.getCellsReference(), a.getCellsReference()) && a.testCollision(shape)) ||
            (Util.setsIntersect(shape.getCellsReference(), b.getCellsReference()) && b.testCollision(shape));
    }

    @Override
    public void shift(double x, double y) {
        a.shift(x, y);
        b.shift(x, y);
    }

    @Override
    public void setPos(double x, double y) {
        a.setPos(x - offX / 2, y - offY / 2);
        b.setPos(x + offX / 2, y + offY / 2);
    }

    @Override
    public void rotate(double r) {
        throw new RuntimeException("Not implemented yet");
       // this.radius += radius;
       // a.setAngle(radius);
      //  b.setAngle(radius);
    }

    @Override
    public void setAngle(double r) {
        throw new RuntimeException("Not implemented yet");
      //  this.radius = radius;
     //   a.setAngle(radius);
      //  b.setAngle(radius);
    }

    @Override
    public void draw(GraphicsContext gc) {
        a.draw(gc);
        b.draw(gc);
    }
}
