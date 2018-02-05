package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.model.GameModel;
import game.model.game.model.Model;
import game.model.game.model.worldObject.entity.Entity;

public class MovingStrat extends UpdateStrat {

    @Override
    public UpdateData initUpdateData() {
        return new UpdateData();
    }

    MovingStrat(Model model) {
        super(model);
    }

    @Override
    protected void update(Entity entity, GameModel model, double seconds) {
        entity.data.x += entity.data.updateData.velocity.getVx() * seconds;
        entity.data.y += entity.data.updateData.velocity.getVy() * seconds;
    }
}
