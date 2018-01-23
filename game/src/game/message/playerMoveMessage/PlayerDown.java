package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;
import game.model.Game.WorldObject.Entity.Player;

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

    /**
     * Moves the player downwards.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        ((Player) model.getEntityById(id)).down();
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
