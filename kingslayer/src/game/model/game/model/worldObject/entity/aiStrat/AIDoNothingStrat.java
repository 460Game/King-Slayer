package game.model.game.model.worldObject.entity.aiStrat;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

public class AIDoNothingStrat extends AIStrat {
    public static final AIDoNothingStrat SINGLETON = new AIDoNothingStrat();

    @Override
    public AIData makeAIData() {
        return null;
    }

    @Override
    public void updateAI(Entity entity, ServerGameModel model, double seconds) {
        //Do nothing
    }
}
