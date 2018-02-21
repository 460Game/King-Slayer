package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.message.toServer.RequestEntityRequest;
import game.model.game.map.ClientMapGenerator;
import game.model.game.map.Tile;
import game.model.game.model.gameState.GameState;
import game.model.game.model.gameState.Loading;
import game.model.game.model.gameState.Running;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;

public class ClientGameModel extends GameModel {

    private TeamResourceData resourceData = new TeamResourceData();

    Team winningTeam = null;
    Team losingTeam = null;


    public ClientGameModel(Model server) {
        super(new ClientMapGenerator());
        this.server = server;
    }

    private long localPlayer;

    public Entity getLocalPlayer() {
        return this.getEntity(localPlayer);
    }

    public void setResourceData(TeamResourceData data) {
        resourceData = data;
    }

    public TeamResourceData getResourceData() {
        return resourceData;
    }

    public void setLocalPlayer(long localPlayer) {
        Log.info("Set local player");
        this.localPlayer = localPlayer;
    }

    private Model server;

    @Override
    public void processMessage(Message m) {
        if(server == null)
            throw new RuntimeException("Cannot receive message before init()");
        if (m.sendToClient())
            this.queueMessage(m);
        if (m.sendToServer())
            server.processMessage(m);
    }

    public void init(Team team, Role role, TeamRoleEntityMap map, Tile[][] gameMap) {
        this.setLocalPlayer(map.getEntity(team, role));
        for(int x = 0; x < getMapWidth(); x++)
            for(int y = 0; y < getMapHeight(); y++)
                this.getCell(x,y).setTile(gameMap[x][y], this);
        state = Running.SINGLETON;
    }

    @Override
    public long nanoTime() {
        return server.nanoTime();
    }

    @Override
    public String toString() {
        return "Client game model";
    }

    public void requestEntityFromServer(long id) {
        server.processMessage(new RequestEntityRequest(id));
    }

    @Override
    public void execute(Consumer<ServerGameModel> serverAction, Consumer<ClientGameModel> clientAction) {
        clientAction.accept(this);
    }

    public void changeWinningTeam(Team team) {
        if (winningTeam == getLocalPlayer().getTeam()) return; //already won
        winningTeam = team;
    }

    public void changeLosingTeam(Team team) {
        if (losingTeam == getLocalPlayer().getTeam()) return; //already lost
        losingTeam = team;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public Team getLosingTeam() {
        return losingTeam;
    }

    private GameState state = Loading.SINGLETON;

    public GameState getState() {
        return state;
    }
}
