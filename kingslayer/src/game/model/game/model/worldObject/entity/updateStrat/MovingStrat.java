package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.model.GameModel;
import game.model.game.model.Model;
import game.model.game.model.worldObject.entity.Entity;

public class MovingStrat extends UpdateStrat {

    public static final UpdateStrat SINGLETON = new MovingStrat();
    @Override
    protected void update(Entity entity, GameModel model, double seconds) {
        entity.translateX(entity.getVelocity().getVx() * seconds);
        entity.translateY(entity.getVelocity().getVy() * seconds);
    }

    @Override
    public void init(Entity entity) {

    }
}
