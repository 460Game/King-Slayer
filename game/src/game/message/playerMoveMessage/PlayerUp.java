package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;

/**
 * Message sent by a client to tell the server to stop the player
 * moving vertically on the game map.
 */
public class PlayerUp extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     */
    public PlayerUp(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public PlayerUp() {

    }

    @Override
    public void executeServer(ServerGameModel model) {
        ((TestPlayer)model.getEntityById(id)).up();
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
