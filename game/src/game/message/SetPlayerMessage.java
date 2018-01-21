package game.message;

import com.esotericsoftware.minlog.Log;
import game.model.Game.Model.ClientGameModel;
import game.model.Game.WorldObject.Entity.TestPlayer;

/**
 * Message sent to set a player on a client's game model. This message
 * is sent by the server.
 */
public class SetPlayerMessage implements ToClientMessage {

    /**
     * ID of the player.
     */
    private long playerId;

    /**
     * Default constructor needed for serialization.
     */
    public SetPlayerMessage() {

    }

    /**
     * Constructor for a message, given a player ID.
     * @param playerId ID of the player
     */
    public SetPlayerMessage(long playerId) {
        this.playerId = playerId;
    }

    /**
     * Constructor for a message, given a player.
     * @param player the player to be set
     */
    public SetPlayerMessage(TestPlayer player) {
        this.playerId = player.getId();
    }

    /**
     * Sets the player in the client's game model.
     * @param model the game model on the client
     */
    @Override
    public void executeClient(ClientGameModel model) { model.setLocalPlayer(Math.toIntExact(playerId)); } // TODO temp fix
}
