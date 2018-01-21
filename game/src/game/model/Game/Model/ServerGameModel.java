package game.model.Game.Model;

import com.esotericsoftware.jsonbeans.Test;
import com.esotericsoftware.minlog.Log;
import game.message.*;
import game.model.Game.Map.ServerMapGenerator;
import game.model.Game.WorldObject.Entity.Entity;
import game.model.Game.WorldObject.Entity.TestPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import static Util.Const.*;

public class ServerGameModel extends GameModel {

    public ServerGameModel() {
        super(new ServerMapGenerator(GRID_X_SIZE, GRID_Y_SIZE));
    }

    private Collection<? extends Model> clients = null;

    @Override
    public void processMessage(Message m) {
        if(clients == null)
            throw new RuntimeException("Cannot receive message before init()");
        if (m.sendToServer())
            this.queueMessage(m);
        if (m.sendToClient())
            clients.forEach(model -> model.processMessage(m));
    }

    @Override
    public long nanoTime() {
        return System.nanoTime();
    }

    public void init(Collection<? extends Model> clients) {
        this.clients = clients;

        // Send map to client
        for(Model client : clients)
            for (int i = 0; i < this.getMapWidth(); i++)
                for (int j = 0; j < this.getMapWidth(); j++)
                    client.processMessage(new SetTileMessage(i, j, this.getTile(i, j)));

        //Send all enttities to clients
        for(Entity entity : this.getAllEntities())
            this.processMessage(new SetEntityMessage(entity));

        ArrayList<TestPlayer> players = new ArrayList<>();
        for (Entity entity : this.getAllEntities()) {
            if(entity instanceof TestPlayer)
                players.add((TestPlayer) entity);
        }

        int i = 0;
        // Send player to client
        for(Model model : clients) {

            Log.info("sending set player message");
            model.processMessage(new SetPlayerMessage(players.get(i)));
        }
    }

}
