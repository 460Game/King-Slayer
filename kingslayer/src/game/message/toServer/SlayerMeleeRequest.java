package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.slayer.SlayerData;
import music.MusicPlayer;

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
        if(model.getEntity(id) == null)
            return;
        //TODO: make sure I can simply set it here
        SlayerData curSlayerData =
                SlayerData.copyOf((SlayerData)model.getEntity(id).get(Entity.EntityProperty.SLAYER_DATA));
        if (curSlayerData.meleeLastTime > 0 || curSlayerData.magic < SlayerData.meleeCost) {
            return;
        } else {
            curSlayerData.meleeLastTime = 0.5;
            curSlayerData.meleeAngle = angle;
            curSlayerData.magic -= SlayerData.meleeCost;
        }

        model.getEntity(id).set(Entity.EntityProperty.SLAYER_DATA, curSlayerData);

        MusicPlayer.playChargeSound();

    }
}
