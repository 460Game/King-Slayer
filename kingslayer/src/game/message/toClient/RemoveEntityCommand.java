package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent to remove an entity from a client's game model. This message
 * is sent by the server.
 */
public class RemoveEntityCommand implements ToClientCommand {

    /**
     * ID of the entity to be removed.
     */
    private long entityID;

    /**
     * Default constructor needed for serialization.
     */
    public RemoveEntityCommand() {}

    /**
     * Constructor of a message, given an entity ID to be removed.
     * @param id ID of the entity to be removed
     */
    public RemoveEntityCommand(long id) {
        this.entityID = id;
    }

    /**
     * Constructor of a message, given an entity to be removed.
     * @param entity entity to be removed
     */
    public RemoveEntityCommand(Entity entity) {
        this.entityID = entity.id;
    }

    /**
     * Remove the references of the local entity with the given ID in the client
     * model.
     * @param model the game model on the client.
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.removeByID(entityID);
    }
}
