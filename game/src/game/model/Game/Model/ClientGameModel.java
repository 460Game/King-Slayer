package game.model.Game.Model;

import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.Game.Map.ClientMapGenerator;
import game.model.Game.Map.MapGenerator;
import game.model.Game.WorldObject.Entity.TestPlayer;

public class ClientGameModel extends GameModel {

    public ClientGameModel() {
        super(new ClientMapGenerator());
    }

    private long localPlayer;

    public TestPlayer getLocalPlayer() {
        return (TestPlayer) this.getEntityById(localPlayer);
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
        return System.nanoTime(); //TODO @tian @tian fix this!!! https://en.wikipedia.org/wiki/Clock_synchronization
    }

    public void init(Model server) {
        this.server = server;
    }
}
