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

    private long localid;

    /**
     * Tiles in the game world.
     */
    private Tile[][] gameMap;

    /**
     * Constructor for a message to initialize a game, given a client's team,
     * role, and mapping from team and role to ID.
     * @param team team of the client
     * @param role role of the client
     * @param fgdhtghgthdfgh
     * @param gameMap tiles in the game created by the server
     */
    public InitGameCommand(Team team, Role role, long localid, Tile[][] gameMap) {
        this.team = team;
        this.role = role;
        this.localid = localid;
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
        model.init(team, role, localid, gameMap);
    }
}
