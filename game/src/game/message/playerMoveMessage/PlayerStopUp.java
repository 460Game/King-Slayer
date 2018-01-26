package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;
import game.model.Game.WorldObject.Entity.Player;

/**
 * Message sent by a client to tell the server to stop the player
 * moving vertically upwards on the game map. This is sent whenever the
 * up key is let go.
 */
public class PlayerStopUp extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the stop message.
     *
     * @param id player ID that send the message
     */
    public PlayerStopUp(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public PlayerStopUp() {

    }

    /**
     * Stops the player's upwards movement.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        ((Player) model.getEntityById(id)).stopUp();
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
