package game.message.toServer;

import game.message.toClient.RemoveEntityCommand;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Request sent by a client to the server to remove an entity
 * from the game world.
 */
public class RemoveEntityRequest extends ActionRequest {

    /**
     * ID of the entity.
     */
    private long id;

    /**
     * Constructor for a message, given an id.
     * @param id id of the entity being removed
     */
    public RemoveEntityRequest(long id) {
        this.id = id;
    }

    /**
     * Constructor for a message, given an entity
     * @param e entity being removed
     */
    public RemoveEntityRequest(Entity e) {
        this.id = e.id;
    }

    /**
     * Default constructor needed for serialization.
     */
    private RemoveEntityRequest() {}

    /**
     * Request the entity with the given id from the server game model.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        Entity entity = model.getEntity(id);
        if (entity != null) {
            model.removeByID(id);
            // TODO duplicate remove entity in here and remove by id
            model.getClients().forEach(client -> client.processMessage(new RemoveEntityCommand(entity)));
        }
    }
}
