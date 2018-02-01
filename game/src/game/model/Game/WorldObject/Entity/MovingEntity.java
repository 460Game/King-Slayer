package game.model.Game.WorldObject.Entity;

import game.model.Game.Model.GameModel;

/**
 * Class that defines an entity that moves.
 */
public abstract class MovingEntity extends Entity {

    /**
     * Default constructor needed for serialization.
     */
    MovingEntity() {
        super();
    }

    /**
     * Constructor for the entity with the given game model
     * @param model current game model
     */
    MovingEntity(GameModel model) {
        super(model);
    }

    @Override
    public abstract void collision(GameModel model, StationaryEntity collidesWith);

    @Override
    public abstract void collision(GameModel model, MovingEntity collidesWith);
}
