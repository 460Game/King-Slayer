package game.model.game.model.gameState;

import game.model.game.model.GameModel;
import game.model.game.model.team.Team;

public class TeamWinGameState extends GameState {
    public Team team;
    public TeamWinGameState(Team team) {
        this.team = team;
    }

    @Override
    public void processState(GameModel model) {

    }
}
