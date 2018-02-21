package game.model.game.model.worldObject.entity.aiStrat;

import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

public abstract class AIStrat {

    public abstract void init(Entity entity);

    public abstract void updateAI(Entity entity, ServerGameModel model, double seconds);
}
