package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.message.toServer.RequestEntityMessage;
import game.model.game.map.ClientMapGenerator;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;

public class ClientGameModel extends GameModel {

    private TeamResourceData resourceData = new TeamResourceData();

    public ClientGameModel(Model server) {
        super(new ClientMapGenerator());
        this.server = server;
    }

    private long localPlayer;

    public Entity getLocalPlayer() {
        return this.getEntityById(localPlayer);
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

    @Override
    public long nanoTime() {
        return server.nanoTime();
    }

    @Override
    public String toString() {
        return "Client game model";
    }

    public void requestEntityFromServer(long id) {
        server.processMessage(new RequestEntityMessage(id));
    }
}
