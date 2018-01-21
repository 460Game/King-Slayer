package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;

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
        ((TestPlayer)model.getEntityById(id)).stopVert();
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
