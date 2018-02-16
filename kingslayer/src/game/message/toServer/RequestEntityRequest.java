package game.message.toServer;

import game.message.toClient.NewEntityCommand;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent by a client to ask the server for an entity
 * on the game map.
 */
public class RequestEntityRequest implements ToServerRequest {

    /**
     * ID of the entity.
     */
    private long id;

    /**
     * Constructor for a message, given an id
     * @param id id of the entity being requested.
     */
    public RequestEntityRequest(long id) {
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    private RequestEntityRequest() {}

    /**
     * Request the entity with the given id from the server game model.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        Entity entity = model.getEntity(id);
       if(entity != null)
            model.processMessage(new NewEntityCommand(entity));
    }
}
