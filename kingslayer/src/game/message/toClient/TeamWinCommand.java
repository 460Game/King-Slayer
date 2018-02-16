package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Team;

public class TeamWinCommand implements ToClientCommand {
    public Team winningTeam;
    public TeamWinCommand(Team winningTeam) {
        this.winningTeam = winningTeam;
    }
    public TeamWinCommand() {
        this.winningTeam = null;
    }
    @Override
    public void executeClient(ClientGameModel model) {
        model.changeWinningTeam(winningTeam);
    }
}
