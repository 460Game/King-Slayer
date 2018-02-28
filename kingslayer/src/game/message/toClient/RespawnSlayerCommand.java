package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.entities.Players;

public class RespawnSlayerCommand implements ToClientCommand {
    double x, y;
    Team team;
    public RespawnSlayerCommand() {}

    public RespawnSlayerCommand(double x, double y, Team team) {
        this.x = x;
        this.y = y;
        this.team = team;
    }

    @Override
    public void executeClient(ClientGameModel model) {
        model.respawnCnt = 0;
        model.clientLoseControl = false;
        model.processMessage(new NewEntityCommand(Players.makeSlayer(x, y, team)));
    }
}
