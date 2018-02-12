package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent to create an entity in a client's game model. This message
 * is sent by the server.
 */
public class NewEntityMessage implements ToClientMessage {

    /**
     * Entity to be created.
     */
    private Entity entity;

    /**
     * Constructor of a message, given an entity to be created.
     * @param entity entity to be created
     */
    public NewEntityMessage(Entity entity) {
        this.entity = entity;
    }

    /**
     * Default constructor needed for serialization.
     */
    private NewEntityMessage(){}

    /**
     * Add the entity to the client model.
     * @param model the game model on the client.
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.setEntity(entity);
    }
}
