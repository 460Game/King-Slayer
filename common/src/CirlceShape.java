import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Set;

public class CirlceShape extends Shape {

    double x,y,r;

    transient Set<Tile> memo = null;

    @Override
    public Set<Tile> getTiles(Map map) {
        if(memo != null )
            return memo;
        memo = new HashSet<>();

        //TODO support moving the shape
        //then this will have to update accordingly

        for(int i = (int)(x - r); i <= Math.ceil(x + r); i++){
            for(int j = (int)(y - r); j <= Math.ceil(y + r); j++){
                if((i+0.5-x)*(i+0.5-x) + (j+0.5-y)*(j+0.5-y) <= (r+0.70710678118)*(r+0.70710678118))
                    memo.add(map.get(i,j));
            }
        }


        return memo;
    }

    @Override
    public boolean testCollision(Shape shape) {
        if(shape instanceof CirlceShape) {
            CirlceShape circle = (CirlceShape) shape;
            return Util.dist(this.x, circle.x, this.y , circle.y) <= this.r + circle.r;
        } else if(shape instanceof RectShape) {
            RectShape rect = (RectShape) shape;
            //cirlce rectange collisions are such a pain!
            return false; //TODO!!
        } else if(shape instanceof CompositeShape) {
            CompositeShape compositeShape = (CompositeShape) shape;
            return compositeShape.testCollision(this);
        }

        throw new RuntimeException("Dont know how to collide circle with this other thing");
    }

    CirlceShape(double x, double y, double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.fillOval(x,y,r,r);
    }
}
