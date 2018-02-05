package game.message;

import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.entities.Player;

/**
 * Message sent by a client to tell the server to move the player
 * on the game map.
 */
public class SetVelocityMessage extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * String of the direction the player wants to move.
     */
    private String dir;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     * @param dir direction of the movement
     */
    public SetVelocityMessage(long id, String dir) {
        super();
        this.id = id;
        this.dir = dir;
    }

    /**
     * Default constructor needed for serialization.
     */
    public SetVelocityMessage() {

    }

    /**
     * Moves the player in a certain direction.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        model.getEntityById(id).move(dir);
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
