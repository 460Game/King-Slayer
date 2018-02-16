package game.model.game.model.gameState;

import game.model.game.model.GameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;

import java.util.function.Consumer;

public class TeamWinGameState extends GameState {
    public Team team;
    public TeamWinGameState(Team team) {
        this.team = team;
    }

    @Override
    public void processState(GameModel model) {
        Consumer<ServerGameModel> serverConsumer = (server) -> {
//            server.teamWin();
        };
        model.execute(serverConsumer, (client) -> {
            //client do nothing
        });
    }
}
