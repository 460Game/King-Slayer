package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public abstract class SoftCollisionStrat extends CollisionStrat{

    public abstract void collisionSoft(GameModel model, Entity a, Entity b);
    protected abstract void collisionHard(GameModel model, Entity a, Entity b);

    public final void collision(GameModel model, Entity a, Entity b) {
        if(a.getCollideType() == CollideType.HARD) {
            collisionHard(model, a, b);
        } else if(a.getCollideType() == CollideType.SOFT) {
            collisionSoft(model, a, b);
        }
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.SOFT;
    }
}
