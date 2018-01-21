package game.model.Game.Model;

import com.esotericsoftware.kryonet.Server;
import game.message.Message;
import game.model.Game.Map.ClientMapGenerator;
import game.model.Game.Map.MapGenerator;
import game.model.Game.WorldObject.Entity.TestPlayer;

public class ClientGameModel extends GameModel {


    public ClientGameModel(Model server) {
        super(new ClientMapGenerator());
        this.server = server;
    }

    private long localPlayer;

    public TestPlayer getLocalPlayer() {
        return (TestPlayer) this.getEntityById(localPlayer);
    }

    public void setLocalPlayer(long player) {
        localPlayer = localPlayer;
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
        return System.nanoTime(); //TODO @tian @tian fix this!!! https://en.wikipedia.org/wiki/Clock_synchronization
    }

}
