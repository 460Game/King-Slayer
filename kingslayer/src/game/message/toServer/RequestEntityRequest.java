package game.message.toServer;

import game.message.toClient.NewEntityCommand;
import game.model.game.model.ServerGameModel;

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
        model.processMessage(new NewEntityCommand(model.getEntityById(id)));
    }
}
