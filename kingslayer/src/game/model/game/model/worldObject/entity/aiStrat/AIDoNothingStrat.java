package game.model.game.model.worldObject.entity.aiStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

public class AIDoNothingStrat extends AIStrat {
    public static final AIDoNothingStrat SINGLETON = new AIDoNothingStrat();

    @Override
    public AIData initAIData() {
        return null;
    }

    @Override
    public void updateAI(Entity entity, GameModel model) {
        //Do nothing
    }

    public static AIDoNothingStrat make() {
        return SINGLETON;
    }
}
