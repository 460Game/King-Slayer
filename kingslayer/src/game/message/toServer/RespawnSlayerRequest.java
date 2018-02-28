package game.message.toServer;

import game.message.toClient.NewEntityCommand;
import game.message.toClient.RespawnSlayerCommand;
import game.model.game.model.Model;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Players;

public class RespawnSlayerRequest extends ActionRequest {
    Team myTeam;
    String name;
    public RespawnSlayerRequest() {}
    public RespawnSlayerRequest(Team team, String name) {
        myTeam = team;
        this.name = name;
    }
    @Override
    public void executeServer(ServerGameModel model) {
        Entity king = model.getEntity(model.teamRoleEntityMap.getEntity(myTeam, Role.KING));
        double x = king.get(Entity.EntityProperty.X);
        double y = king.get(Entity.EntityProperty.Y);
        Entity entity = Players.makeSlayer(x, y, myTeam);
        entity.add(Entity.EntityProperty.PLAYER_NAME, name);
        model.processMessage(new MakeEntityRequest(entity));
        model.processMessage(new NewEntityCommand(entity));
        model.processMessage(new RespawnSlayerCommand(entity.id));
    }
}