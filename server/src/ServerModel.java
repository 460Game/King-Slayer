import java.io.IOException;

import Command.UpdateCommand;
import Entity.WorldObject;
import Model.GameModel;
import Model.WorldClock;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.Map;

public class ServerModel extends GameModel {


    Map<WorldObject, WorldObject> map;
    WorldClock clock;
    Server server;

    /**
     * Game gameMap
     */
    @Override
    WorldClock getTimer() {
        return null;
    }

    // This holds per connection state.
    public static class GameConnection extends Connection {
        public String usrName;
    }

    /**
     * Constructor
     */
    public ServerModel () {
        server = new Server();
        clock = new WorldClock();
    }

    /**
     * Get the Gamemap (server model)
     * @return
     */
    @Override
    public GameMap getGameMap() {
        return null;
    }

    public void start() throws IOException {
        NetWorkCommon.register(server);

        server.addListener(new Listener() {
            public void received (Connection c, Object obj) {

                // We know all connections for this server are actually ChatConnections.
                GameConnection connection = (GameConnection)c;

//                if (obj instanceof UpdateCommand) {
//                    UpdateCommand updateMsg = (UpdateCommand) obj;
//                    //if (updateMsg.?? == null) return;
//                    updateMsg.execute(map);
//
//                    //Send back
////                    server.sendToAllTCP(msgWithName);
//                    return;
//                }

                if (obj instanceof ActionCommand) {
                    ActionCommand clientActionMsg = (ActionCommand) obj;
                    //if (updateMsg.?? == null) return;
                    clientActionMsg.execute(this);

                    //Send back
//                    server.sendToAllTCP(msgWithName);
                    return;
                }
            }

            //TODO: implement disconnected
            public void disconnected (Connection c) {
                GameConnection connection = (GameConnection)c;
                if (connection.usrName != null) {

                }
            }
        });

        server.bind(NetWorkCommon.port);
        server.start();
    }

    public static void main(String[] args) throws IOException {
        ServerModel serverModel = new ServerModel();
        serverModel.start();
    }

}
