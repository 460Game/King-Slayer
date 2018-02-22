package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Velocity;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.VELOCITY;

public abstract class UpdateStrat {

    public abstract void update(Entity entity, GameModel model);

    public void init(Entity entity) {
        entity.add(VELOCITY, Velocity.NONE);
    }
}
