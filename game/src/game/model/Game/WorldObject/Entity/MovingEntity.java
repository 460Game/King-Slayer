package game.model.Game.WorldObject.Entity;

import game.model.Game.Model.GameModel;

/**
 * Class that defines an entity that moves.
 */
public abstract class MovingEntity extends Entity {

    /**
     * Time of the last update this entity performed.
     */
    private long last_update;

    /**
     * Default constructor needed for serialization.
     */
    MovingEntity() {

    }

    /**
     * Constructor for the entity with the given game model
     * @param model current game model
     */
    MovingEntity(GameModel model) {
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
