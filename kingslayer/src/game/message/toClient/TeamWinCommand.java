package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Team;

/**
 * Message sent by the server to the client saying that a team
 * has won the game.
 */
public class TeamWinCommand implements ToClientCommand {

    /**
     * Team that wins and receives the message.
     */
    private Team winningTeam;

    /**
     * Constructor of a message, given a winning team.
     * @param winningTeam team that won the game
     */
    public TeamWinCommand(Team winningTeam) {
        this.winningTeam = winningTeam;
    }

    /**
     * Default constructor needed for serialization.
     */
    public TeamWinCommand() {
        this.winningTeam = null;
    }

    /**
     * Sets the winning team to the one sent by the server.
     * @param model the game model on the client
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.changeWinningTeam(winningTeam);
    }
}
