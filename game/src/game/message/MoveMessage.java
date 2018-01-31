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
     * Change in y movement.
     */
    private int dy;

    /**
     * Change in x movement.
     */
    private int dx;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     * @param dx change in x movement
     * @param dy change in y movement
     */
    public MoveMessage(long id, int dx, int dy) {
        super();
        this.id = id;
        this.dx = dx;
        this.dy = dy;
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
        ((Player) model.getEntityById(id)).move(dx, dy);
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
