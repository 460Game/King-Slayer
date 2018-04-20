package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;

import static game.model.game.model.team.TeamResourceData.Resource.METAL;
import static game.model.game.model.team.TeamResourceData.Resource.STONE;
import static game.model.game.model.team.TeamResourceData.Resource.WOOD;

public class CheatRequest extends ActionRequest {
    private Team team = null;

    public CheatRequest(Team team)
    {
        this.team = team;
    }

    private CheatRequest() {}

    @Override
    public void executeServer(ServerGameModel model) {
        if (team != null) {
            model.changeResource(team, WOOD, 50000);
            model.changeResource(team, STONE, 50000);
            model.changeResource(team, METAL, 50000);
        }
    }
}
