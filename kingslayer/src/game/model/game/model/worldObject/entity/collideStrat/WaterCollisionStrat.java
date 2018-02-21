package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Defines how water collides with other entities.
 */
public class WaterCollisionStrat extends CollisionStrat {

    /**
     * Only one instance of a water collision strategy is created. All water
     * uses this strategy to collide.
     */
    public static WaterCollisionStrat SINGLETON = new WaterCollisionStrat();

    @Override
    public void collision(GameModel model, Entity a, Entity b) {
        if (b.getCollideType() == CollideType.HARD || b.getCollideType() == CollideType.WATER) {
            throw new RuntimeException("Hard objects colliding " + a.getHitbox() + " and " + b.getHitbox());
        }
    }

    @Override
    public CollideType getCollideType() {
        return CollideType.WATER;
    }
}
