package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Entities;
import game.model.game.model.worldObject.entity.slayer.SlayerData;
import music.MusicPlayer;

/**
 * Message sent by a client to tell the server that it wants
 * to shoot an arrow.
 */
public class ShootArrowRequest extends ActionRequest {

    /**
     * ID of the entity requesting to shoot the arrow.
     */
    private long id;

    /**
     * Angle of movement of the arrow.
     */
    private double angle;

    /**
     * X-coordinate of the arrow.
     */
    private double x;

    /**
     * Y-coordinate of the arrow.
     */
    private double y;

    /**
     * Team that made the shoot arrow request.
     */
    private Team team;

    /**
     * Constructor for a message, given an id.
     * @param id id of the entity wanting to shoot the arrow
     * @param x x-coordinate of the arrow
     * @param y y-coordinate of the arrow
     * @param angle angle of movement of the arrow
     * @param team team of the entity that requested the arrow
     */
    public ShootArrowRequest(long id, double x, double y, double angle, Team team) {
        this.id = id;
        this.angle = angle;
        this.x = x;
        this.y = y;
        this.team = team;
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
        if(model.getEntity(id) == null)
            return;
        SlayerData curSlayerData = SlayerData.copyOf(model.getEntity(id).get(Entity.EntityProperty.SLAYER_DATA));
        if (curSlayerData.magic < SlayerData.arrowCost) {
            return;
        }
        curSlayerData.magic -= SlayerData.arrowCost;
        model.getEntity(id).set(Entity.EntityProperty.SLAYER_DATA, curSlayerData);

        Entity arrow = Entities.makeArrow(x, y, angle, team, model.getEntity(id), curSlayerData.arrowDamage, -1);
        model.makeEntity(arrow);
    }
}
