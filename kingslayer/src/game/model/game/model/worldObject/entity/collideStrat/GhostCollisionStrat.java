package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class GhostCollisionStrat extends CollisionStrat{

    public static GhostCollisionStrat SINGLETON = new GhostCollisionStrat();

    public void collisionSoft(GameModel model, Entity a, Entity b){

    }

    public void collisionHard(GameModel model, Entity a, Entity b) {

    }

    public void collision(GameModel model, Entity a, Entity b) {
        if(b.getCollideType() == CollideType.HARD || b.getCollideType() == CollideType.WATER) {
            collisionHard(model, a, b);
        } else if(b.getCollideType() == CollideType.SOFT) {
            collisionSoft(model, a, b);
        }
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.GHOST;
    }
}
