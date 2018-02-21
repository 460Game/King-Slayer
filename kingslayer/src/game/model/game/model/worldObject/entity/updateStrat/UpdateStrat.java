package game.model.game.model.worldObject.entity.updateStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;

import static util.Const.NANOS_TO_SECONDS;

public abstract class UpdateStrat {

    /**
     * Updates the entity in the game model.
     * @param model current game model
     */
    public void update(Entity entity, GameModel model) {
        if(!entity.has(Entity.EntityProperty.LAST_UPDATE_TIME)) {
            entity.add(Entity.EntityProperty.LAST_UPDATE_TIME, model.nanoTime());
            return;
        }
//        long current_time = model.nanoTime();
//        update(entity, model, NANOS_TO_SECONDS * (current_time - entity.data.updateData.lastUpdate));
        update(entity, model, NANOS_TO_SECONDS * entity.timeDelta);
//        entity.data.updateData.lastUpdate = current_time;
        entity.set(Entity.EntityProperty.LAST_UPDATE_TIME, entity.<Long>get(Entity.EntityProperty.LAST_UPDATE_TIME) + entity.timeDelta);
    }

    protected abstract void update(Entity entity, GameModel model, double seconds);

    public abstract void init(Entity entity);
}
