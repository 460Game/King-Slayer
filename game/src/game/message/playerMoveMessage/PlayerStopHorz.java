package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

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


    @Override
    public void executeServer(ServerGameModel model) {
        // TODO temporary fix, need to fix id issue
        model.getPlayer(Math.toIntExact(id)).stopHorz();
        model.processMessage(new SetEntityMessage(model.getPlayer(Math.toIntExact(id))));
    }
}
