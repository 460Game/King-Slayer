package game.message;

import game.model.Game.Model.ClientGameModel;
import game.model.Game.WorldObject.Entity.Entity;

public class RemoveEntityMessage implements ToClientMessage {

    long entityID;
    RemoveEntityMessage() {}
    RemoveEntityMessage(Entity entity) {
        this.entityID = entity.getId();
    }

    /*
    should removeContents references to the corasponding local entity from the model
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.removeByID(entityID);
    }
}
