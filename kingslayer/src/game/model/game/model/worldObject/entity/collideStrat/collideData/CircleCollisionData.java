package game.model.game.model.worldObject.entity.collideStrat.collideData;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import util.Util;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Set;
import static util.Const.*;
import static util.Util.toDrawCoords;

/**
 * A collisionData that is a circle.
 */
public class CircleCollisionData extends CollisionData {

    /**
     * Radius of the circular collisionData.
     */
    private double radius;

    /**
     * Default constructor needed for serialization.
     */
    public CircleCollisionData() {

    }

    /**
     * Constructor of a circle collisionData with given coordinates and radius.
     * @param radius radius of the circle
     */
    public CircleCollisionData(double radius) {
        this.radius = radius;
    }

    /**
     * Gets the radius of the circle.
     * @return radius of the circle
     */
    public double getRadius() { return radius; }

    @Override
    public Set<GridCell> getCells(Entity entity, GameModel gameMap) {
        Set<GridCell> set = new HashSet<>();

        // Look at all close cells to the circle and check if the circle
        // overlaps with them.
        for(int i = (int) (entity.data.x - radius); i <= Math.ceil(entity.data.x + radius); i++)
            for(int j = (int) (entity.data.y - radius); j <= Math.ceil(entity.data.y + radius); j++) {
                double dx = entity.data.x - Math.max(i, Math.min(entity.data.x, i + 1)); // Get distance from farthest x to center
                double dy = entity.data.y - Math.max(j, Math.min(entity.data.y, j + 1)); // Get distance from farthest y to center
                if ((dx * dx + dy * dy + 0.01) < radius * radius) {
                    set.add(gameMap.getCell(i, j));
                }
            }

        return set;
    }

    @Override
    public void draw(GraphicsContext gc, Entity entity) {
        gc.fillOval(toDrawCoords(entity.data.x - 0.5 * radius),
            toDrawCoords(entity.data.y - 0.5 * radius),
            toDrawCoords(radius), toDrawCoords(radius));
    }

    @Override
    public double getRadius(double angle) {
        return radius;
    }
}
