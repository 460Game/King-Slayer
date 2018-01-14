package game.model;

import game.message.Message;
import game.model.Game.GameModel;
import game.model.Game.Tile.Tile;
import javafx.scene.canvas.GraphicsContext;

import java.util.Collection;
import java.util.UUID;

public abstract class ProcessorForwarderModel implements IModel {

    boolean isClient = false;
    boolean isServer = false;

    public ProcessorForwarderModel(boolean isServer, Collection<IModel> others) {
        if(isServer) {
            this.isServer = true;
            this.clients = others;
        } else {
            this.isClient = true;
            this.servers = others;
        }
    }

    private Collection<IModel> servers;
    private Collection<IModel> clients;

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
