import java.util.Set;

public class CompositeShape extends Shape {

    Shape a;
    Shape b;

    @Override
    public Set<Tile> getTiles(Map map) {
        Set<Tile> set = a.getTiles(map);
        set.addAll(b.getTiles(map));
        return set;
    }

    @Override
    public boolean testCollision(Shape shape) {
        return a.testCollision(shape) || b.testCollision(shape);
    }
}
