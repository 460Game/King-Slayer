package game.model.game.model.worldObject.entity.deathStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public abstract class DeathStrat {
    public abstract void handleDeath(GameModel model, Entity entity);
}
