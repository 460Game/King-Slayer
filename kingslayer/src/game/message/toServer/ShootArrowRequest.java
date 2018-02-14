package game.message.toServer;

import game.message.toClient.NewEntityCommand;
import game.model.game.model.ServerGameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Entities;

/**
 * Created by ryanhan on 2/14/18.
 */
public class ShootArrowRequest extends ActionRequest {

    /**
     * ID of the entity requesting to shoot the arrow.
     */
    private long id;

    private double angle;

    private double x;

    private double y;

    /**
     * Constructor for a message, given an id.
     * @param id id of the entity wanting to shoot the arrow.
     * @param
     */
    public ShootArrowRequest(long id, double x, double y, double angle) {
        this.id = id;
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    /**
     * Default constructor needed for serialization.
     */
    private ShootArrowRequest() {}

    /**
     * Request to shoot an arrow from the entity with the given id from the server game model.
     * @param model the game model on the game server
     */
    @Override
    public void executeServer(ServerGameModel model) {
        Entity arrow = Entities.makeArrow(x, y, angle);
        model.makeEntity(arrow);
//        model.processMessage(new NewEntityCommand(model.getEntityById(arrow.id)));
        model.getClients().forEach(client -> client.processMessage(new NewEntityCommand(model.getEntityById(arrow.id))));
    }
}
