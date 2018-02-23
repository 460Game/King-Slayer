package game.message.toServer;

import game.message.toClient.SetEntityCommand;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Velocity;

/**
 * Message sent by a client to tell the server to move the player
 * on the game map.
 */
public class GoDirectionRequest extends ActionRequest {

    /**
     * ID to distinguish player that sent the message.
     */
    private long id;

    /**
     * Angle that the player wants to move.
     */
    private double angle;

    /**
     * Constructor for the move message.
     * @param id player ID that send the message
     * @param angle direction of the movement
     */
    public GoDirectionRequest(long id, double angle) {
        super();
        this.id = id;
        this.angle = angle;
    }

    /**
     * Default constructor needed for serialization.
     */
    public GoDirectionRequest() {

    }

    /**
     * Moves the player in a certain direction.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        Entity e = model.getEntity(id);
        e.setVelocity(Velocity.NONE.withMagnitude(3).withAngle(angle));
        //TODO where is max speed
    }
}
