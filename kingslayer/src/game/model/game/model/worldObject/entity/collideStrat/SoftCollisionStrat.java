package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.collideData.CollisionData;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;

public class SoftCollisionStrat extends CollisionStrat{

    void collisionSoft(GameModel model, Entity b);
    void collisionHard(GameModel model, Entity b);

    public final void collision(GameModel model, Entity b) {
        if(b.getCollideType() == CollideType.HARD) {
            collisionHard(model, b);
        } else if(b.getCollideType() == CollideType.SOFT) {
            collisionSoft(model, b);
        }
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.SOFT;
    }
}
