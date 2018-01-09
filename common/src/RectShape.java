import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Set;

public class RectShape extends Shape {

    //x,y are center of the rectangle
   private double x,y,w,h;
    //todo support rotated rectangles

     transient Set<Tile> memo = null;

    @Override
    public Set<Tile> getTiles(Map map) {
        if(memo != null)
            return memo;
        //TODO support moving the shape
        memo = new HashSet<>();
        for(int i = (int)(x - w); i <= Math.ceil(x + w); i++){
            for(int j = (int)(y - h); j <= Math.ceil(y + h); j++){
                memo.add(map.get(i,j));
            }
        }
        return memo;
    }

    @Override
    public boolean testCollision(Shape shape) {
        if(shape instanceof CirlceShape) {
            //circle cricle
            CirlceShape circle = (CirlceShape) shape;
            return false; //TODO this
           // return Util.dist(this.x, this.y, circle.x , circle.y) <= this.r + circle.r;
        } else if(shape instanceof RectShape) {
            //rectangle rectangle
            RectShape rect = (RectShape) shape;
            //cirlce rectange collisions are such a pain!
            return false; //TODO!!
        } else if(shape instanceof CompositeShape) {
            CompositeShape compositeShape = (CompositeShape) shape;
            return compositeShape.testCollision(this);
        }

        throw new RuntimeException("Dont know how to collide circle with this other thing");
    }

    RectShape(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillRect(x,y,w,h);
    }
}
