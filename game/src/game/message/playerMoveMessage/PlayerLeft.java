package game.message.playerMoveMessage;

import game.message.ActionMessage;
import game.message.SetEntityMessage;
import game.model.Game.Model.ServerGameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;

/**
 * Message sent by a client to tell the server to move the player
 * leftwards on the game map.
 */
public class PlayerLeft extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     */
    public PlayerLeft(long id) {
        super();
        this.id = id;
    }

    /**
     * Default constructor needed for serialization.
     */
    public PlayerLeft() {

    }

    @Override
    public void executeServer(ServerGameModel model) {
        ((TestPlayer)model.getEntityById(id)).left();
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
