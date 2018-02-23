package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Request sent by a client to the server that says the client
 * wants to create an entity.
 */
public class MakeEntityRequest implements ToServerRequest {

    /**
     * Entity to be made.
     */
    private Entity entity;

    /**
     * Constructor of the request, given an entity to be made.
     * @param entity entity to be made
     */
    public MakeEntityRequest(Entity entity) {
        this.entity = entity;
    }

    /**
     * Creates the entity in the server.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        model.makeEntity(entity);
    }
}
