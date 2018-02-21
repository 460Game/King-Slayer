package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CellHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;

/**
 * Defines how hard entities, like buildings and walls, collide with other
 * entities. They usually do nothing, unless the hard object is destroyable.
 */
public class HardCollisionStrat extends CollisionStrat {

    /**
     * Only one instance of a hard collision strategy is created. All hard entities
     * use this strategy to collide.
     */
    public static HardCollisionStrat SINGLETON = new HardCollisionStrat();

    /**
     * Performs the collision of entity a with entity b in the game model.
     * Entity a usually will not perform any collision handling. It is an
     * error if two hard entities collide.
     * @param model current model of the game
     * @param a entity to perform collision handling on
     * @param b entity colliding with
     */
    public final void collision(GameModel model, Entity a, Entity b) {
        if(b.getCollideType() == CollideType.HARD || b.getCollideType() == CollideType.WATER) {
            throw new RuntimeException("Hard objects colliding " + a.hitbox + " and " + b.hitbox);
        }
    }
    @Override
    public final CollideType getCollideType() {
        return CollideType.HARD;
    }
}
