package game.message.toServer;

import game.message.toClient.SetEntityMessage;
import game.message.toServer.ActionMessage;
import game.model.game.model.ServerGameModel;

/**
 * Message sent by a client to tell the server to stop the player
 * on the game map.
 */
public class StopMessage extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the stop message.
     * @param id player ID that send the message
     */
    public StopMessage(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public StopMessage() {

    }

    /**
     * Stops the player's movement.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        model.getEntityById(id).data.updateData.velocity.setMagnitude(0);
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
