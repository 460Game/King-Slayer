package game.message;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;

public class NewEntityMessage implements ToClientMessage {

    private Entity entity;

    public NewEntityMessage(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void executeClient(ClientGameModel model) {
        model.setEntity(entity);
    }
}
