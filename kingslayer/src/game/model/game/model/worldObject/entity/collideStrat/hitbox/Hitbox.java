package game.model.game.model.worldObject.entity.collideStrat.hitbox;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Players;
import javafx.scene.paint.Color;
import util.Pos;
import util.Util;
import game.model.game.model.GameModel;
import game.model.game.grid.GridCell;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Math.PI;
import static util.Const.*;
import static util.Util.dist;

/**
 * Shouldn't be drawable! Purely for testing for now.
 * Abstract class that defines a generic getHitbox. All entities on a tile
 * have a getHitbox derived from this class.
 */
public abstract class Hitbox {

    /**
     * Return the set of all cells this getHitbox overlaps with. This method
     * should return a new set every time.
     *
     * @param gameMap current model of the game
     * @return the set of all cells that this getHitbox overlaps with.
     */
    public abstract Set<GridCell> getCells(double x, double y, GameModel gameMap);

    /**
     * Return the set of all cell references this getHitbox overlaps with.
     * This method should return a new set every time.
     */
    public void updateCells(Entity entity, GameModel model) {

        // Check if the entity has moved, so it can draw the cells it is in.
        if (entity.getX() != entity.prevX || entity.getY() != entity.prevY) {
            // Get the set of cells the entity is currently in.
            Set<GridCell> afterSet = entity.getHitbox().getCells(entity.getX(), entity.getY(), model);

//            System.out.println("Player position: " + entity.data.x + ", " + entity.data.y);

//            if (entity.containedIn != null)
//                for (GridCell cell: entity.containedIn)
//                    System.out.println("Before cell: " + cell.getTopLeftX() + ", " + cell.getTopLeftY());
//            for (GridCell cell : afterSet) {
////                System.out.println("After cell: " + cell.getTopLeftX() + ", " + cell.getTopLeftY());
//                for (Entity a : cell.getContents()) {
//                    System.out.println(a.toString());
//                }
//            }

            // Check if the entity is still currently in the same cells that it
            // was previously. If the entity is not currently in a cell it used
            // to be in, remove it from that cell's contents.
            if (entity.containedIn != null)
                for (GridCell cell : entity.containedIn)
                    if (!afterSet.contains(cell))
                        cell.removeContents(model, entity);

            // Add the entity to the cells contents for each cell it is currently in.
            for (GridCell cell : afterSet)
                if (!cell.getContents().contains(entity))
                    cell.addContents(model, entity);

            // Update the cells that it is currently in and its previous x, y coordinates.
            entity.containedIn = afterSet;
            entity.prevX = entity.getX();
            entity.prevY = entity.getY();
        }
    }

    /**
     * Returns true if this getHitbox collides with the given getHitbox. Returns false
     * otherwise.
     *
     * @return true if this getHitbox collides with the specified getHitbox
     */
    public static boolean testCollision(Entity t, Entity o) {
        return testCollision(t.getX(), t.getY(), t.getHitbox(), o.getX(), o.getY(), o.getHitbox());
    }

    public static boolean testCollision(double x1, double y1, Hitbox box1, double x2, double y2, Hitbox box2) {
        double angle = Util.angle2Points(x1, y2, x2, y2);
        return box1.getRadius(angle) + box2.getRadius(angle + PI) >
            dist(x1, y1, x2, y2);
    }

    /**
     * Color in different colors for entities that are colliding. Primarily
     * used for debugging hitboxes.
     *
     * @param gc     graphics context to draw
     * @param entity entity being drawn
     */
    public void draw(GraphicsContext gc, Entity entity) {
        if (entity.inCollision)
            gc.setFill(Color.color(1, 0, 0, 0.5));
        else
            gc.setFill(Color.color(1, 1, 1, 0.5));
        drawShape(gc, entity);
    }

    /**
     * Draw the shape of the entity in the game world. Primarily used for
     * debugging hitboxes.
     *
     * @param gc     graphics context to draw
     * @param entity entity being drawn
     */
    public abstract void drawShape(GraphicsContext gc, Entity entity);

    /**
     * Gets the distance from the center of the getHitbox to the farthest edge at
     * the given angle.
     *
     * @param angle angle at which to find the "radius"
     * @return the distance from the center of the getHitbox to the edge at the angle
     */
    public abstract double getRadius(double angle);

    /**
     * Get the width of the object (the length of the horizontal line going through
     * the center of the getHitbox.
     *
     * @return the width of the object
     */
    public double getWidth() {
        return getRadius(ANGLE_LEFT) + getRadius(ANGLE_RIGHT);
    }

    /**
     * Get the height of the object (the length of the vertical line going through
     * the center of the getHitbox.
     *
     * @return the height of the object
     */
    public double getHeight() {
        return getRadius(ANGLE_UP) + getRadius(ANGLE_DOWN);
    }


    public Stream<Entity> getCollidesWith(GameModel model, double x, double y) {
        Set<GridCell> collection = this.getCells(x, y, model);
        return collection.stream().flatMap(cell -> cell.getContents().stream()).filter(entity -> entity.checkCollision(this, x, y));
    }
}
