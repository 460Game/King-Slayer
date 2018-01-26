package game.model.Game.WorldObject.Entity;

import Util.Util;
import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import game.model.Game.Grid.GridCell;
import game.model.Game.WorldObject.Drawable;
import game.model.Game.WorldObject.Shape.Shape;
import game.model.Game.WorldObject.Team;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static Util.Const.TILE_PIXELS;

/**
 * Defines an abstract entity in the game world.
 */
public abstract class Entity implements Drawable {

    /**
     * Current health of the entity.
     */
    private double health;

    /**
     * Current speed of the entity.
     */
    private double speed;

    /**
     * Current angle with which the entity is moving.
     */
    private double angle;

    /**
     * Team of this entity.
     */
    private Team team;

    /**
     * ID of this entity.
     */
    private long id = Util.random.nextLong();

    /**
     * Time of the last update this entity performed.
     */
    private long last_update;

    /**
     * Default constructor needed for serialization.
     */
    Entity() {

    }

    /**
     * Constructor for the entity with the given game model
     * @param model current game model
     */
    Entity(GameModel model) {
        last_update = model.nanoTime();
    }

    /**
     * Gets the current health of the entity.
     * @return the current health of the entity
     */
    public double getHealth() {
        return health;
    }

    /**
     * Sets the current health of the entity to the amount specified.
     * @param health new health of the entity
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Gets the current speed of the entity.
     * @return the current speed of the entity
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Sets the current speed of the entity to the amount specified.
     * @param speed new speed of the entity
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Gets the current angle of movement of the entity.
     * @return the current angle of movement of the entity
     */
    public double getMovementAngle() {
        return angle;
    }

    /**
     * Sets the current angle of movement of the entity to the angle
     * specified.
     * @param angle new angle of movement
     */
    public void setMovementAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Gets the current health of the entity.
     * @return the current health of the entity
     */
    public Team getTeam() {
        return team;
    }

    /**
     * Sets the team of the entity to the team specified
     * @param team new team of the entity
     */
    public void setTeam(Team team) {
        this.team = team;
    }

    /**
     * Perform collision with the other entity in the current game model.
     * @param model current game model
     * @param collidesWith stationary entity that this entity collides with
     */
    public abstract void collision(GameModel model, StationaryEntity collidesWith);

    /**
     * Perform collision with the other entity in the current game model.
     * @param model current game model
     * @param collidesWith moving entity that this entity collides with
     */
    public abstract void collision(GameModel model, MovingEntity collidesWith);

    /**
     * Sets this entity to be a copy of another entity.
     * @param other the entity to be copied
     */
    public void copyOf(Entity other) {
        this.health = other.health;
        this.angle = other.angle;
        this.speed = other.speed;
        this.team = other.team;
        this.id = other.id;
        this.last_update = other.last_update;
    }

    /**
     * Gets the shape of the entity.
     * @return the shape of the entity
     */
    public abstract Shape getShape();

    /**
     * Gets the id of the entity.
     * @return the id of the entity
     */
    public long getId() {
        return id;
    }

    /**
     * Updates the entity in the game model.
     * @param time time since the last update
     * @param model current game model
     */
    public abstract void update(long time, GameModel model);

    /**
     * Updates the entity in the game model.
     * @param model current game model
     */
    public void update(GameModel model) {
        long current_time = model.nanoTime();
        update(current_time - last_update, model);
        last_update = current_time;

        //TODO this needs to be better
        if(getShape().moved()) {
            Collection<GridCell> afterSet = getShape().getCells(model);

            for (GridCell cell : model.getAllCells())
                if(!afterSet.contains(cell))
                  cell.removeContents(this);
            for (GridCell cell : afterSet)
                cell.addContents(this);
        }
        //TODO this must be more effeceint
    }

    @Override
    public int hashCode() {
        return (int) (id % Integer.MAX_VALUE);
    }

    /**
     * Draws the entity in the game.
     * @param gc context used to draw the entity
     */
    public abstract void draw(GraphicsContext gc);

    /**
     * Draws the entity in the game.
     * @param gc context used to draw the entity
     */
    public void draw(GraphicsContext gc, Image image) {
        gc.drawImage(image,
            this.getX() * TILE_PIXELS - TILE_PIXELS/2,
            this.getY() * TILE_PIXELS - TILE_PIXELS/2 + 25,
            TILE_PIXELS, 1.5*TILE_PIXELS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Entity entity = (Entity) o;
        return this.id == entity.id;
    }

    /**
     * Gets the x-coordinate of the center of the entity.
     * @return the x-coordinate of the center of the entity
     */
    public double getX() {
       return this.getShape().getX();
    }

    /**
     * Gets the y-coordinate of the center of the entity.
     * @return the y-coordinate of the center of the entity
     */
    public double getY() {
        return this.getShape().getY();
    }

    /**
     * Return the set of all cells this entity overlaps with. This method
     * should return a new set every time.
     * @param gameMap current model of the game
     * @return the set of all cells that this entity overlaps with.
     */
    public Collection<GridCell> getCells(GameModel gameMap) {
        return getShape().getCells(gameMap);
    }

    /**
     * Returns true if this entity collides with the specified entity, given that
     * they are on the same cell. Returns false otherwise.
     * @param other the entity to be tested for collisions
     * @return true if this entity collides with the specified entity
     */
    public boolean testCollision(Entity other) {
        return this.getShape().testCollision(other.getShape());
    }

    /**
     * Shift the entity by the delta specified.
     * @param dx shift in x-coordinate
     * @param dy shift in y-coordinate
     */
    public void shift(double dx, double dy) {
        this.getShape().shift(dx,dy);
    }

    /**
     * Sets the position to the coordinates specified.
     * @param x x-coordinate of the center of the entity
     * @param y y-coordinate of the center of the entity
     */
    public void setPos(double x, double y) {
        this.getShape().setPos(x,y);
    }

    /**
     * Rotates the entity by the given angle.
     * @param r angle to rotate the entity by
     */
    public void rotate(double r) {
        this.getShape().rotate(r);
    }

    /**
     * Sets the angle of the entity to the angle specified.
     * @param r new angle of the entity.
     */
    public void setAngle(double r) {
        this.getShape().setAngle(r);
    }
}
