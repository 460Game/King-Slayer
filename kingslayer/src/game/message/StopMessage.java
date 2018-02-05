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
     * Angle of the original movement before stopping.
     */
    private double angle;

    /**
     * Constructor for the stop message.
     * @param id player ID that send the message
     * @param angle original direction before stopping
     */
    public StopMessage(long id, double angle) {
        super();
        this.id = id;
        this.angle = angle;
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
