package game.message.toClient;

import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamRoleEntityMap;

/**
 * This message is sent to the client after the server has sent
 * all the initial data. This initializes the client's game, including
 * the role and team of the client.
 */
public class InitGameCommand implements ToClientCommand {

    /**
     * Team of the client.
     */
    private Team team;

    /**
     * Role of the client.
     */
    private Role role;

    /**
     * Mapping from an entity's role and team to its ID.
     */
    private TeamRoleEntityMap map;

    /**
     * Constructor for a message to initialize a game, given a client's team,
     * role, and mapping from team and role to ID.
     * @param team team of the client
     * @param role role of the client
     * @param map mapping from team and role to ID
     */
    public InitGameCommand(Team team, Role role, TeamRoleEntityMap map) {
        this.team = team;
        this.role = role;
        this.map = map;
    }

    /**
     * Default constructor needed for serialization.
     */
    public InitGameCommand() {

    }

    /**
     * Initialize the game for the client.
     * @param model the game model on the client
     */
    @Override
    public void executeClient(ClientGameModel model) {
        model.init(team, role, map);
    }
}
