package game.message;

import game.model.Game.Model.ClientGameModel;
import game.model.Game.WorldObject.Entity.Entity;

/**
 * Message sent to set an entity on a client's game model. This message
 * is sent by the server.
 */
public class SetEntityMessage implements ToClientMessage {

    /**
     * Entity to be set.
     */
    private Entity entity;

    /**
     * Default constructor needed for serialization.
     */
    public SetEntityMessage() {

    }

    /**
     * Constructor for a message, given an entity.
     * @param entity entity to be set
     */
    public SetEntityMessage(Entity entity) {
        this.entity = entity;
    }

    /**
     * This guy is a tricky one!
     *
     * If entity with same UUID already exists in this model, should copy the new one into it.
     * If it doesn't exist copy the whole thing!
     * TODO
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.setEntity(entity);
    }
}
