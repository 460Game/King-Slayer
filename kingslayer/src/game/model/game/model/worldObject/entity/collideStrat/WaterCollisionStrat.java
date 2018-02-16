package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class WaterCollisionStrat extends CollisionStrat {

    public static WaterCollisionStrat SINGLETON = new WaterCollisionStrat();

    @Override
    public void collision(GameModel model, Entity a, Entity b) {
        if (b.getCollideType() == CollideType.HARD || b.getCollideType() == CollideType.WATER) {
            throw new RuntimeException("Hard objects colliding " + a.data.hitbox + " and " + b.data.hitbox);
        }
    }

    @Override
    public CollideType getCollideType() {
        return CollideType.WATER;
    }
}
