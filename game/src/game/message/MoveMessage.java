package game.message;

import game.model.Game.Model.ServerGameModel;
import game.model.Game.WorldObject.Entity.Player;

/**
 * Message sent by a client to tell the server to move the player
 * on the game map.
 */
public class MoveMessage extends ActionMessage{

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
    public MoveMessage(long id, String dir) {
        super();
        this.id = id;
        this.dir = dir;
    }

    /**
     * Default constructor needed for serialization.
     */
    public MoveMessage() {

    }

    /**
     * Moves the player in a certain direction.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        ((Player) model.getEntityById(id)).move(dir);
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
