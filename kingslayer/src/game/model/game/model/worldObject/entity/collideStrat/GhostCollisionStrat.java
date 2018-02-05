package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public abstract class GhostCollisionStrat extends CollisionStrat{

    abstract void collisionSoft(GameModel model, Entity b);
    abstract void collisionHard(GameModel model, Entity b);

    public final void collision(GameModel model, Entity a, Entity b) {
        if(a.getCollideType() == CollideType.HARD) {
            collisionHard(model, a);
        } else if(a.getCollideType() == CollideType.SOFT) {
            collisionSoft(model, a);
        }
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.GHOST;
    }
}
