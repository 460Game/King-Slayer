package game.model.game.model.worldObject.entity.deathStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class NopDeathStrat extends DeathStrat{
    public static final NopDeathStrat SINGLETON = new NopDeathStrat();

    @Override
    public void handleDeath(GameModel model, Entity entity) {

    }
}
