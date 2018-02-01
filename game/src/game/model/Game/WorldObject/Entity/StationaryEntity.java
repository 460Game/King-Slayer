package game.model.Game.WorldObject.Entity;

import game.model.Game.Model.GameModel;

/**
 * Class that defines an entity that does not move.
 */
public abstract class StationaryEntity extends Entity {

    /**
     * Default constructor needed for serialization.
     */
    StationaryEntity() {
        super();
    }

    /**
     * Constructor for the entity with the given game model
     * @param model current game model
     */
    StationaryEntity(GameModel model) {
        super(model);
    }

    @Override
    public void collision(GameModel model, StationaryEntity collidesWith) {
        // Do nothing because it is stationary.
    }

    @Override
    public void collision(GameModel model, MovingEntity collidesWith) {
        // Do nothing
    }
}
