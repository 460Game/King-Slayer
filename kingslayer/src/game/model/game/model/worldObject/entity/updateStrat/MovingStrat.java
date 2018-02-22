package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class MovingStrat extends UpdateStrat {

    public static final UpdateStrat SINGLETON = new MovingStrat();
    @Override
    public void update(Entity entity, GameModel model) {
        entity.translateX(entity.getVelocity().getVx() * entity.timeDelta);
        entity.translateY(entity.getVelocity().getVy() * entity.timeDelta);
    }
}
