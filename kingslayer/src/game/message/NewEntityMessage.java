package game.message;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;

public class NewEntityMessage implements ToClientMessage {

    private Entity entity;

    public NewEntityMessage(Entity entity) {
        this.entity = entity;
    }

    private NewEntityMessage(){}

    @Override
    public void executeClient(ClientGameModel model) {
        Log.info("Received new entity " + entity.id);
        model.setEntity(entity);
    }
}
