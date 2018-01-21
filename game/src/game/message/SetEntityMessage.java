package game.message;

import game.model.Game.Model.ClientGameModel;
import game.model.Game.WorldObject.Entity.Entity;

public class SetEntityMessage implements ToClientMessage {

    private Entity entity;

    public SetEntityMessage() {
    }
    public SetEntityMessage(Entity entity) {
        this.entity = entity;
    }

        /**
         * this guy is a tricky one!
         *
         * if enttity with same UUID already exists in this model, should copy the new one into it
         * if dosnt exist copy the whole thing!
         */
    @Override
    public void executeClient(ClientGameModel model) {
        model.setEntity(entity);
    }
}
