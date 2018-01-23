package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;
import game.model.Game.WorldObject.Entity.Player;

/**
 * Message sent by a client to tell the server to stop the player
 * moving horizontally on the game map. This is sent whenever the
 * left/right key is let go.
 */
public class PlayerStopHorz extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the stop message.
     * @param id player ID that send the message
     */
    public PlayerStopHorz(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public PlayerStopHorz() {

    }

    /**
     * Stops the player's horizontal movement.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        ((Player) model.getEntityById(id)).stopHorz();
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
