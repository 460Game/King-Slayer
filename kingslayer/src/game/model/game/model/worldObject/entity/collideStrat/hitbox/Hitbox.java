package game.model.game.model.worldObject.entity.collideStrat.hitbox;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Players;
import javafx.scene.paint.Color;
import util.Util;
import game.model.game.model.GameModel;
import game.model.game.grid.GridCell;
import javafx.scene.canvas.GraphicsContext;

import java.util.Set;

import static java.lang.Math.PI;
import static util.Const.*;
import static util.Util.dist;

/**
 * Shouldn't be drawable! Purely for testing for now.
 * Abstract class that defines a generic hitbox. All entities on a tile
 * have a hitbox derived from this class.
 */
public abstract class Hitbox {

    /**
     * Return the set of all cells this hitbox overlaps with. This method
     * should return a new set every time.
     * @param gameMap current model of the game
     * @return the set of all cells that this hitbox overlaps with.
     */
    public abstract Set<GridCell> getCells(Entity entity, GameModel gameMap);

    /**
     * Return the set of all cell references this hitbox overlaps with.
     * This method should return a new set every time.
     */
    public void updateCells(Entity entity, GameModel model) {

        // Check if the entity has moved, so it can update the cells it is in.
        if(entity.data.x != entity.prevX || entity.data.y != entity.prevY) {
            // Get the set of cells the entity is currently in.
            Set<GridCell> afterSet = entity.data.hitbox.getCells(entity, model);

//            if (entity.containedIn != null)
//                for (GridCell cell: entity.containedIn)
//                    System.out.println("Before cell: " + cell.getCenterX() + ", " + cell.getCenterY());
//            for (GridCell cell : afterSet)
//                System.out.println("After cell: " + cell.getCenterX() + ", " + cell.getCenterY());

            // Check if the entity is still currently in the same cells that it
            // was previously. If the entity is not currently in a cell it used
            // to be in, remove it from that cell's contents.
            if (entity.containedIn != null)
                for (GridCell cell : entity.containedIn)
                    if (!afterSet.contains(cell))
                        cell.removeContents(entity);

            // Add the entity to the cells contents for each cell it is currently in.
            for (GridCell cell : afterSet)
                if (!cell.getContents().contains(entity))
                    cell.addContents(entity);

            // Update the cells that it is currently in and its previous x, y coordinates.
            entity.containedIn = afterSet;
            entity.prevX = entity.data.x;
            entity.prevY = entity.data.y;

//            System.out.println("Player position: " + entity.data.x + ", " + entity.data.y);
        }
    }

    /**
     * Returns true if this hitbox collides with the given hitbox. Returns false
     * otherwise.
     * @return true if this hitbox collides with the specified hitbox
     */
    public static boolean testCollision(Entity t, Entity o) {
        double angle = Util.angle2Points(t.data.x, t.data.y, o.data.x, o.data.y);
        return t.data.hitbox.getRadius(angle) + o.data.hitbox.getRadius(angle + PI) <
                dist(t.data.x, t.data.y, o.data.x, o.data.y);
    }

    /**
     * Color in different colors for entities that are colliding. Primarily
     * used for debugging hitboxes.
     * @param gc graphics context to draw
     * @param entity entity being drawn
     */
    public void draw(GraphicsContext gc, Entity entity) {
        if(entity.inCollision)
           gc.setFill(Color.color(1, 0, 0, 0.5));
        else
            gc.setFill(Color.color(1, 1, 1, 0.5));
        drawShape(gc,entity);
    }

    /**
     * Draw the shape of the entity in the game world. Primarily used for
     * debugging hitboxes.
     * @param gc graphics context to draw
     * @param entity entity being drawn
     */
    public abstract void drawShape(GraphicsContext gc, Entity entity);

    /**
     * Gets the distance from the center of the hitbox to the farthest edge at
     * the given angle.
     * @param angle angle at which to find the "radius"
     * @return the distance from the center of the hitbox to the edge at the angle
     */
    public abstract double getRadius(double angle);

    /**
     * Get the width of the object (the length of the horizontal line going through
     * the center of the hitbox.
     * @return the width of the object
     */
    public double getWidth() {
        return getRadius(ANGLE_LEFT) + getRadius(ANGLE_RIGHT);
    }

    /**
     * Get the height of the object (the length of the vertical line going through
     * the center of the hitbox.
     * @return the height of the object
     */
    public double getHeight() {
        return getRadius(ANGLE_UP) + getRadius(ANGLE_DOWN);
    }
}
