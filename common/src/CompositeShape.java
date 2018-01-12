import GameMap.GameMap;
import Tile.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.Set;

public class CompositeShape extends Shape {

    Shape a;
    double offX;
    double offY; // shape a is -offX/2, -offY/2 offset from center, shape b is offX/2 offY/2 from center
    Shape b;

    @Override
    public Set<Tile> getTiles(GameMap gameMap) {
        Set<Tile> set = a.getTiles(gameMap);
        set.addAll(b.getTiles(gameMap));
        return set;
    }

    @Override
    public boolean testCollision(Shape shape) {
        return a.testCollision(shape) || b.testCollision(shape);
    }

    @Override
    public void shift(double x, double y) {
        a.shift(x,y);
        b.shift(x,y);
    }

    @Override
    public void setPos(double x, double y) {
        a.setPos(x - offX/2, y - offY/2);
        b.setPos(x + offX/2, y + offY/2);
    }

    @Override
    public void draw(GraphicsContext gc) {
        a.draw(gc);
        b.draw(gc);
    }
}
