package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.entities.Players;

public class RespawnSlayerCommand implements ToClientCommand {
    long id;
    private RespawnSlayerCommand() {}

    public RespawnSlayerCommand(long id) {
        this.id = id;
    }

    @Override
    public void executeClient(ClientGameModel model) {
        if(model.clientLoseControl && model.getTeam() == model.getEntity(id).getTeam())
        model.setLocalPlayer(id);
        model.clientLoseControl = true;
        model.respawnCnt = 0;
    }
}
