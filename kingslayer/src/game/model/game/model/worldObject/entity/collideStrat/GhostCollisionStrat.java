package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Defines how ghosts collide with other entities in the world.
 */
public class GhostCollisionStrat extends CollisionStrat{

    /**
     * Only one instance of a ghost collision strategy is created. All ghosts
     * use this strategy to collide.
     */
    public static GhostCollisionStrat SINGLETON = new GhostCollisionStrat();

    /**
     * Performs the soft collision of entity a with entity b in the game model:
     * b is given to have a soft collision type. The collision strategy used
     * is the strategy of entity a: this only performs the handling for entity a.
     * @param model current model of the game
     * @param a entity to perform collision handling on
     * @param b entity colliding with
     */
    public void collisionSoft(GameModel model, Entity a, Entity b){
        // Do nothing.
    }

    /**
     * Performs the hard collision of entity a with entity b in the game model:
     * b is given to have a hard collision type. The collision strategy used
     * is the strategy of entity a: this only performs the handling for entity a.
     * @param model current model of the game
     * @param a entity to perform collision handling on
     * @param b entity colliding with
     */
    public void collisionHard(GameModel model, Entity a, Entity b) {
        // Do nothing.
    }

    @Override
    public void collision(GameModel model, Entity a, Entity b) {
        // Water is treated as a hard entity to ghosts.
        if(b.getCollideType() == CollideType.HARD || b.getCollideType() == CollideType.WATER) {
            collisionHard(model, a, b);
        } else if(b.getCollideType() == CollideType.SOFT) {
            collisionSoft(model, a, b);
        }
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.GHOST;
    }
}
