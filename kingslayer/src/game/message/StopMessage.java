package game.message;

import game.model.game.model.ServerGameModel;

/**
 * Message sent by a client to tell the server to stop the player
 * on the game map.
 */
public class StopMessage extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * String of the direction the player wants to stop.
     */
    private String dir;

    /**
     * Constructor for the stop message.
     * @param id player ID that send the message
     * @param dir direction to stop movement
     */
    public StopMessage(long id, String dir) {
        super();
        this.id = id;
        this.dir = dir;
    }

    /**
     * Default constructor needed for serialization.
     */
    public StopMessage() {

    }

    /**
     * Stops the player's movement.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        ((Player) model.getEntityById(id)).stop(dir);
        model.processMessage(new SetEntityMessage(model.getEntityById(id)));
    }
}
