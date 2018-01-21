package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

/**
 * Message sent by a client to tell the server to stop the player
 * moving vertically on the game map. This is sent whenever the
 * up/down key is let go.
 */
public class PlayerStopVert extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the stop message.
     * @param id player ID that send the message
     */
    public PlayerStopVert(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public PlayerStopVert() {

    }

    @Override
    public void executeServer(ServerGameModel model) {
        // TODO temporary fix, need to fix id issue
        model.getPlayer(Math.toIntExact(id)).stopVert();
        model.processMessage(new SetEntityMessage(model.getPlayer(Math.toIntExact(id))));
    }
}
