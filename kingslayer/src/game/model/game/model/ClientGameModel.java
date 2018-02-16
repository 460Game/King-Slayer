package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.message.toServer.RequestEntityRequest;
import game.model.game.map.ClientMapGenerator;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.team.TeamRoleEntityMap;
import game.model.game.model.worldObject.entity.Entity;

import java.util.function.Consumer;

public class ClientGameModel extends GameModel {

    private TeamResourceData resourceData = new TeamResourceData();

    Team winningTeam = Team.NEUTRAL;
    Team losingTeam = Team.NEUTRAL;

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

    public void init(Team team, Role role, TeamRoleEntityMap map) {
        this.setLocalPlayer(map.getEntity(team, role));
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
        if (winningTeam == getLocalPlayer().team) return; //already won
        winningTeam = team;
    }

    public void changeLosingTeam(Team team) {
        if (losingTeam == getLocalPlayer().team) return; //already lost
        losingTeam = team;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public Team getLosingTeam() {
        return losingTeam;
    }
}
