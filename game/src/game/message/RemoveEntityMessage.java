package game.message;

import game.model.Game.GameModel;
import game.model.Game.WorldObject.Entity.Entity;

public class RemoveEntityMessage implements ToClientMessage {

    long entityID;
    RemoveEntityMessage() {}
    RemoveEntityMessage(Entity entity) {
        this.entityID = entity.getId();
    }

    @Override
    public void execute(GameModel model) {
        model.removeByID(entityID);
    /*
    should removeContents references to the corasponding local entity from the model
     */
    }
}
