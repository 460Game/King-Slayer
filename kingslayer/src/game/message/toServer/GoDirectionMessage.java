package game.message.toServer;

import game.message.toClient.SetEntityMessage;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;

/**
 * Message sent by a client to tell the server to move the player
 * on the game map.
 */
public class GoDirectionMessage extends ActionMessage {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * String of the direction the player wants to move.
     */
    private double angle;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     * @param angle direction of the movement
     */
    public GoDirectionMessage(long id, double angle) {
        super();
        this.id = id;
        this.angle = angle;
    }

    /**
     * Default constructor needed for serialization.
     */
    public GoDirectionMessage() {

    }

    /**
     * Moves the player in a certain direction.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        Entity e = model.getEntityById(id);
        e.data.updateData.velocity.setAngle(angle);
        e.data.updateData.velocity.setMagnitude(e.data.updateData.maxSpeed);
        model.processMessage(new SetEntityMessage(e));
    }
}
