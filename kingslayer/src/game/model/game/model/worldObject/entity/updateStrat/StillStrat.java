package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.model.GameModel;
import game.model.game.model.Model;
import game.model.game.model.worldObject.entity.Entity;

public class StillStrat extends UpdateStrat {

    private static final StillStrat SINGLETON = new StillStrat();

    @Override
    protected void update(Entity entity, GameModel model, double seconds) {
        //Do nothing
    }

    public static StillStrat make() {
        return SINGLETON;
    }
}
