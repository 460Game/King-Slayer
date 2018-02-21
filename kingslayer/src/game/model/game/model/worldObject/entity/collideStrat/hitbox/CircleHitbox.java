package game.model.game.model.worldObject.entity.collideStrat.hitbox;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Set;

import static util.Util.toDrawCoords;

/**
 * A getHitbox that is a circle.
 */
public class CircleHitbox extends Hitbox {

    /**
     * Radius of the circular getHitbox.
     */
    private double radius;

    /**
     * Default constructor needed for serialization.
     */
    public CircleHitbox() {

    }

    /**
     * Constructor of a circle getHitbox with given coordinates and radius.
     * @param radius radius of the circle
     */
    public CircleHitbox(double radius) {
        this.radius = radius;
    }

    /**
     * Gets the radius of the circle.
     * @return radius of the circle
     */
    public double getRadius() { return radius; }

    @Override
    public Set<GridCell> getCells(double x, double y, GameModel gameMap) {
        Set<GridCell> set = new HashSet<>();

        double r = radius + 0.5 ;//+ 0.01;
        // Look at all close cells to the circle and check if the circle
        // overlaps with them.
        for(int i = (int) (x - r); i <= (int) (x + r); i++)
            for(int j = (int) (y - r); j <= (int) (y + r); j++) {
                double dx = x - Math.max(i, Math.min(x, i + 1)); // Get distance from farthest x to center
                double dy = y - Math.max(j, Math.min(y, j + 1)); // Get distance from farthest y to center

                // 0.01 allowed for extra overlap, helps with collisions
                if ((dx * dx + dy * dy) < r * r) {
                    set.add(gameMap.getCell(i, j));
                }
            }

        return set;
    }

    @Override
    public void drawShape(GraphicsContext gc, Entity entity) {
        gc.fillOval(toDrawCoords(entity.data.x - 0.5 * radius),
            toDrawCoords(entity.data.y - 0.5 * radius),
            toDrawCoords(radius), toDrawCoords(radius));
    }

    @Override
    public double getRadius(double angle) {
        return radius;
    }
}
