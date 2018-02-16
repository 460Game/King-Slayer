package game.model.game.model.worldObject.entity.deathStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class SlayerDeathStrat extends DeathStrat {
    public final static SlayerDeathStrat SINGLETON = new SlayerDeathStrat();
    @Override
    public void handleDeath(GameModel model, Entity entity) {
        System.out.println("slayer dies");
    }
}
