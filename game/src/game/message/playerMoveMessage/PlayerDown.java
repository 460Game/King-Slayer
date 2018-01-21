package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

/**
 * Message sent by a client to tell the server to move the player
 * downwards on the game map.
 */
public class PlayerDown extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     */
    public PlayerDown(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public PlayerDown() {

    }

    @Override
    public void executeServer(ServerGameModel model) {
        // TODO temporary fix, need to fix id issue
        model.getPlayer(Math.toIntExact(id)).down();
        model.processMessage(new SetEntityMessage(model.getPlayer(Math.toIntExact(id))));
    }
}
