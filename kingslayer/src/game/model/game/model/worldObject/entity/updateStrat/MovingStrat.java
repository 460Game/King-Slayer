package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.model.GameModel;
import game.model.game.model.Model;
import game.model.game.model.worldObject.entity.Entity;

public class MovingStrat extends UpdateStrat {

    public static final UpdateStrat SINGLETON = new MovingStrat();

    @Override
    public void init() {

    }

    @Override
    protected void update(Entity entity, GameModel model, double seconds) {
        entity.data.x += entity.data.updateData.velocity.getVx() * seconds;
        entity.data.y += entity.data.updateData.velocity.getVy() * seconds;
    }
}
