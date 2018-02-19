package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

public class MakeEntityRequest implements ToServerRequest {
    private Entity entity;

    public MakeEntityRequest(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void executeServer(ServerGameModel model) {
        model.makeEntity(entity);
    }
}
