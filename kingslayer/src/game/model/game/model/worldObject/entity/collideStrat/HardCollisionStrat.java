package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CellHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;

public class HardCollisionStrat extends CollisionStrat{

    private static HardCollisionStrat SINGLTON = new HardCollisionStrat();

    @Override
    public Hitbox initCollisionData() {
        return new CellHitbox();
    }

    public final void collision(GameModel model, Entity a, Entity b) {
        if(a.getCollideType() == CollideType.HARD)
            throw new RuntimeException("HARD objects overlapping");
        //else no-op
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.HARD;
    }

    public static HardCollisionStrat make() {
        return SINGLTON;
    }
}
