package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public abstract class ProjectileCollisionStrat extends CollisionStrat {

    public abstract void collisionSoft(GameModel model, Entity a, Entity b);
    public abstract void collisionWater(GameModel model, Entity a, Entity b);
    protected abstract void collisionHard(GameModel model, Entity a, Entity b);

    public final void collision(GameModel model, Entity a, Entity b) {
        if(b.getCollideType() == CollideType.HARD) {
            collisionHard(model, a, b);
        } else if(b.getCollideType() == CollideType.SOFT) {
            collisionSoft(model, a, b);
        } else if (b.getCollideType() == CollideType.WATER) {
            collisionWater(model, a, b);
        }
    }

    public final CollideType getCollideType() {
        return CollideType.PROJECTILE;
    }
}
