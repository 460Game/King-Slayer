package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Defines the general strategy that entities use to collide in the game.
 */
public abstract class CollisionStrat {

    /**
     * All possible types of collisions in the game.
     */
    public static enum CollideType{
        /**
         * Blocks cells, can not be pushed or overlap, like a building, and
         * does not do any collision resolution logic itself. It is an error if
         * two hard objects overlap.
         */
        HARD,

        /**
         * Does not block cells and can be pushed, like a minion or hero.
         */
        SOFT,

        /**
         * Does not block cells, and does not affect movement normally. It collides
         * with other things, but things do not collide with it.
         */
        GHOST,

        /**
         * Similar to a ghost collision in that this collides with other entities, but
         * entities do not collide with it. Can go over water and similar tiles.
         */
        PROJECTILE,

        /**
         * Like a hard collision type, but projectiles can go over it.
         */
        WATER
    }

    /**
     * Performs the collision of entity a with entity b in the game model.
     * The collision strategy used is the strategy of entity a: this only
     * performs the handling for entity a.
     * @param model current model of the game
     * @param a entity to perform collision handling on
     * @param b entity colliding with
     */
    public abstract void collision(GameModel model, Entity a, Entity b);

    /**
     * Gets the collision type of the collision strategy.
     * @return the collision type of the collision strategy
     */
    public abstract CollideType getCollideType();

}
