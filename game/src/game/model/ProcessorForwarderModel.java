package game.model;

import game.message.Message;
import game.model.Game.GameModel;
import game.model.Game.Grid.GridCell;
import game.model.Game.Tile.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

public abstract class ProcessorForwarderModel implements IModel {

    boolean isClient = false;
    boolean isServer = false;

    public ProcessorForwarderModel(boolean isServer, Collection<? extends IModel> others) {
        if(isServer) {
            this.isServer = true;
            this.clients = others;
            this.servers = Collections.EMPTY_SET;
        } else {
            this.isClient = true;
            this.servers = others;
            this.clients = Collections.EMPTY_SET;
        }
    }

    private Collection<? extends IModel> servers;
    private Collection<? extends IModel> clients;

    public abstract GameModel getGameModel();

    @Override
    public void processMessage(Message m) {
        Class<? extends Message> clazz = m.getClass();

        //proccess
        if(m.sendToServer() && isServer)
            m.execute(getGameModel());

        if(m.sendToClient() && isClient)
            m.execute(getGameModel());

        //forward
        if(m.sendToServer())
            for(IModel model : servers)
                model.processMessage(m);

        if(m.sendToClient())
            for(IModel model : clients)
                model.processMessage(m);
    }

    public abstract void removeByID(UUID entityID);
}
