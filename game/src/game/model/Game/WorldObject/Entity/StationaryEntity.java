package game.model.Game.WorldObject.Entity;

import game.model.Game.Model.GameModel;

/**
 * Class that defines an entity that does not move.
 */
public abstract class StationaryEntity extends Entity {

    /**
     * Time of the last update this entity performed.
     */
    private long last_update;

    /**
     * Default constructor needed for serialization.
     */
    StationaryEntity() {
        this.setVelocity(0);
        this.setMovementAngle(0);
    }

    /**
     * Constructor for the entity with the given game model
     * @param model current game model
     */
    StationaryEntity(GameModel model) {
        this();
        last_update = model.nanoTime();
    }

    @Override
    public void collision(GameModel model, StationaryEntity collidesWith) {
        // Do nothing
    }

    @Override
    public void collision(GameModel model, MovingEntity collidesWith) {
        // Do nothing
    }
}
