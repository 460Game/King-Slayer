package game.message.toClient;

import game.model.game.map.Tile;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.*;

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
     * Tiles in the game world.
     */
    private Tile[][] gameMap;

    /**
     * Constructor for a message to initialize a game, given a client's team,
     * role, and mapping from team and role to ID.
     * @param team team of the client
     * @param role role of the client
     * @param map mapping from team and role to ID
     * @param gameMap tiles in the game created by the server
     */
    public InitGameCommand(Team team, Role role, TeamRoleEntityMap map, Tile[][] gameMap) {
        this.team = team;
        this.role = role;
        this.map = map;
        this.gameMap = gameMap;
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
        model.init(team, role, map, gameMap);
    }
}
