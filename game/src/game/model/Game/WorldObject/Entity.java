package game.model.Game.WorldObject;

import Util.Util;
import game.model.Game.GameModel;
import game.model.Game.Grid.GridCell;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Defines an abstract entity in the game world.
 */
public abstract class Entity {

    /**
     * Current health of the entity.
     */
    private int health;

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
    public int getHealth() {
        return health;
    }

    /**
     * Sets the current health of the entity to the amount specified.
     * @param health new health of the entity
     */
    public void setHealth(int health) {
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
     * @param collidesWith entity that this entity collides with
     */
    public abstract void collision(GameModel model, Entity collidesWith);

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


    public void update(GameModel model) {

        //note that getCells returns a new instance every time
     //   Collection<GridCell> beforeSet = getShape().getCells(model);
    //    Collection<GridCell> toRemove = getShape().getCells(model);

        long current_time = model.nanoTime();
        update(current_time - last_update, model);
        last_update = current_time;

        Collection<GridCell> afterSet = getShape().getCells(model);

     //   toRemove.removeAll(afterSet); //to remove
      //  afterSet.removeAll(beforeSet); //to add

        for(GridCell cell : model.getAllCells())
            cell.remove(this);
        for(GridCell cell : afterSet)
            cell.add(this);
    }

    @Override
    public int hashCode() {
        return (int) (id % Integer.MAX_VALUE);
    }

    public abstract void draw(GraphicsContext gc);

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

    public double getX() {
       return this.getShape().getX();
    }

    public double getY() {
        return this.getShape().getY();
    }

    /**
     * this should return the set of all tiles this shape overlaps with
     * returns a new set every time
     * @param gameMap
     * @return
     */
    public Collection<GridCell> getCells(GameModel gameMap) {
        return getShape().getCells(gameMap);
    }

    /**
     * given that they are on same cell, do they collide?
     * @param
     * @return
     */
    public boolean testCollision(Entity other) {
        return this.getShape().testCollision(other.getShape());
    }

    /**
     * shift the shape by the delta
     * @param dx
     * @param dy
     */
    public void shift(double dx, double dy) {
        this.getShape().shift(dx,dy);
    }

    public void setPos(double x, double y) {
        this.getShape().setPos(x,y);
    }


    public void rotate(double r) {
        this.getShape().rotate(r);
    }

    public void setAngle(double r) {
        this.getShape().setAngle(r);
    }
}
