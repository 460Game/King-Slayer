package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public abstract class GhostCollisionStrat extends CollisionStrat{

    abstract void collisionSoft(GameModel model, Entity b);
    abstract void collisionHard(GameModel model, Entity b);

    public final void collision(GameModel model, Entity b) {
        if(b.getCollideType() == CollideType.HARD) {
            collisionHard(model, b);
        } else if(b.getCollideType() == CollideType.SOFT) {
            collisionSoft(model, b);
        }
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.GHOST;
    }
}
