package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent to create an entity in a client's game model. This message
 * is sent by the server.
 */
public class NewEntityCommand implements ToClientCommand {

    /**
     * Entity to be created.
     */
    private Entity entity;

    /**
     * Constructor of a message, given an entity to be created.
     * @param entity entity to be created
     */
    public NewEntityCommand(Entity entity) {
        if(entity == null)
            throw new RuntimeException("Server cannot tell client to add null entity");
        this.entity = entity;
    }

    /**
     * Default constructor needed for serialization.
     */
    private NewEntityCommand(){}

    /**
     * Add the entity to the client model.
     * @param model the game model on the client.
     */
    @Override
    public void executeClient(ClientGameModel model) {
        if(entity == null)
            throw new RuntimeException("Cleint Cannot add null entity");
        model.setEntity(entity);
    }
}
