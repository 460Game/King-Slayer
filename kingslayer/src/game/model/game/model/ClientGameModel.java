package game.model.game.model;

import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.message.RequestEntityMessage;
import game.model.game.map.ClientMapGenerator;
import game.model.game.model.worldObject.entity.Entities.KingPlayer;
import game.model.game.model.worldObject.entity.Entities.Player;
import game.model.game.model.worldObject.entity.Entities.SlayerPlayer;

public class ClientGameModel extends GameModel {

    public ClientGameModel(Model server) {
        super(new ClientMapGenerator());
        this.server = server;
    }

    private long localPlayer;

    public Player getLocalPlayer() {
        if (this.getEntityById(localPlayer).getClass().equals(KingPlayer.class)) {
            return (KingPlayer) this.getEntityById(localPlayer);
        } else {
            return (SlayerPlayer) this.getEntityById(localPlayer);
        }
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
