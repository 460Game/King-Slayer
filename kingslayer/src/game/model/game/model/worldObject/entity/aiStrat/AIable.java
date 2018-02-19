package game.model.game.model.worldObject.entity.aiStrat;

import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;

public interface AIable {
    void updateAI(ServerGameModel model);
}
