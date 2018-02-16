package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CellHitbox;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;

public class HardCollisionStrat extends CollisionStrat {

    public static HardCollisionStrat SINGLETON = new HardCollisionStrat();

    public final void collision(GameModel model, Entity t, Entity o) {
        if(o.getCollideType() == CollideType.HARD || o.getCollideType() == CollideType.WATER) {
            throw new RuntimeException("Hard objects colliding " + t.data.hitbox + " and " + o.data.hitbox);
        }
    }
    @Override
    public final CollideType getCollideType() {
        return CollideType.HARD;
    }
}
