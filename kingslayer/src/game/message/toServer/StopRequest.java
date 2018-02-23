package game.message.toServer;

import game.message.toClient.SetEntityCommand;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent by a client to tell the server to stop the player
 * on the game map.
 */
public class StopRequest extends ActionRequest {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the stop message.
     * @param id player ID that send the message
     */
    public StopRequest(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public StopRequest() {

    }

    /**
     * Stops the player's movement.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        if(model.getEntity(id).has(Entity.EntityProperty.VELOCITY))
            model.getEntity(id).setVelocity(model.getEntity(id).getVelocity().withMagnitude(0));
           // model.processMessage(new SetEntityCommand(model.getEntity(id)));
    }
}
