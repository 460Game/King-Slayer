package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamRoleEntityMap;

/**
 * This message is sent to the client after the server has sent
 * all the initial data.
 */
public class InitGameMessage implements ToClientMessage {

    private Team team;

    private Role role;

    private TeamRoleEntityMap map;

    public InitGameMessage(Team team, Role role, TeamRoleEntityMap map) {
        this.team = team;
        this.role = role;
        this.map = map;
    }

    public InitGameMessage() {

    }

    @Override
    public void executeClient(ClientGameModel model) {
        model.init(team, role, map);
    }
}
