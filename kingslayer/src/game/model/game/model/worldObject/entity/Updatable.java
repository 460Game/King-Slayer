package game.model.game.model.worldObject.entity;

import game.model.game.model.GameModel;

/**
 * Interface used for entities that can be updated throughout the game.
 */
public interface Updatable {

    /**
     * Update the entity to update the current state of the game.
     * @param model current model of the game
     */
    void update(GameModel model);
}
