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

public abstract class Entity {

    public abstract void collision(GameModel model, Entity collidesWith);

    private long id = Util.random.nextLong();
    private long last_update;

    public void copyOf(Entity other) {
        this.id = other.id;
        this.last_update = other.last_update;
    }


    public abstract Shape getShape();

    public long getId() {
        return id;
    }

    Entity() {
    }
    Entity(GameModel model) {
        last_update = model.nanoTime();
    }

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
