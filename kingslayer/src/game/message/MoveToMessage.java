package game.message;

import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entities.Player;

/**
 * Message sent by a client to tell the server to move the player
 * to a certain cell.
 */
public class MoveToMessage extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * The cell the player wants to move to.
     */
    private GridCell cell;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     * @param cell destination cell
     */
    public MoveToMessage(long id, GridCell cell) {
        super();
        this.id = id;
        this.cell = cell;
    }

    /**
     * Default constructor needed for serialization.
     */
    public MoveToMessage() {

    }

    /**
     * Moves the player to the specified cell.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        ((Player) model.getEntityById(id)).moveTo(cell);
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}