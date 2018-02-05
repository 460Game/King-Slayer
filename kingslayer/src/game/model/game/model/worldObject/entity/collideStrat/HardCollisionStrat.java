package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class HardCollisionStrat extends CollisionStrat{

    public final void collision(GameModel model, Entity b) {
        if(b.getCollideType() == CollideType.HARD)
            throw new RuntimeException("HARD objects overlapping");
        //else no-op
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.HARD;
    }

}
