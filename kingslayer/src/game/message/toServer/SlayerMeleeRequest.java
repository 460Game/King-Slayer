package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;

public class SlayerMeleeRequest extends ActionRequest {
    /**
     * ID of the entity requesting to melee.
     */
    private long id;


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

    private double angle;

    private SlayerMeleeRequest() {}

    public SlayerMeleeRequest(long id, double x, double y, double angle, Team team) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.team = team;
    }
    @Override
    public void executeServer(ServerGameModel model) {
//        model.getEntity(id).setOrAdd();
    }
}
