package game.message.toServer;

import com.esotericsoftware.kryonet.Server;
import game.model.game.model.ClientGameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent to create an entity in a client's game model. This message
 * is sent by the server.
 */
public class MakeEntityMessage implements ToServerMessage {

    /**
     * Entity to be made.
     */
    private Entity entity;

    /**
     * Constructor of a message, given an entity to be created.
     * @param entity entity to be created
     */
    public MakeEntityMessage(Entity entity) {
        this.entity = entity;
    }

    /**
     * Default constructor needed for serialization.
     */
    private MakeEntityMessage(){}

    /**
     * Make the entity in the server model.
     * @param model the game model on the server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        model.makeEntity(entity);
    }
}
