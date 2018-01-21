package game.model.Game.Model;

import game.message.Message;
import game.model.Game.Map.ClientMapGenerator;
import game.model.Game.Map.MapGenerator;
import game.model.Game.WorldObject.Entity.TestPlayer;

public class ClientGameModel extends GameModel {

    public ClientGameModel() {
        super(new ClientMapGenerator());
    }

    private int localPlayer;

    public TestPlayer getLocalPlayer() {
        return this.getPlayer(localPlayer);
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

    public void init(Model server) {
        this.server = server;
    }
}
