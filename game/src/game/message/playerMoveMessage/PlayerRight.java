package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;

/**
 * Message sent by a client to tell the server to move the player
 * rightwards on the game map.
 */
public class PlayerRight extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     */
    public PlayerRight(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public PlayerRight(){

    }


    @Override
    public void executeServer(ServerGameModel model) {
        // TODO temporary fix, need to fix id issue
        model.getPlayer(Math.toIntExact(id)).right();
        model.processMessage(new SetEntityMessage(model.getPlayer(Math.toIntExact(id))));
    }
}
