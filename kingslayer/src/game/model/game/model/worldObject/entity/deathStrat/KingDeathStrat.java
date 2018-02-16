package game.model.game.model.worldObject.entity.deathStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class KingDeathStrat extends DeathStrat {
    public final static KingDeathStrat SINGLETON = new KingDeathStrat();
    @Override
    public void handleDeath(GameModel model, Entity entity) {
        System.out.println("Death checkpoint!");
    }
}
