import java.io.IOException;

import Command.UpdateMessage;
import Entity.WorldObject;
import Model.GameModel;
import Model.WorldClock;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.util.Map;

public abstract class ServerModel extends GameModel {

    /**
     * Game gameMap
     */
    Map<WorldObject, WorldObject> map;

    // This holds per connection state.
    public static class GameConnection extends Connection {
        public String usrName;
    }

    Server server;


    public ServerModel () {
        server = new Server();

    }
    public void start() throws IOException {
        NetWorkCommon.register(server);

        server.addListener(new Listener() {
            public void received (Connection c, Object obj) {

                // We know all connections for this server are actually ChatConnections.
                GameConnection connection = (GameConnection)c;

                if (obj instanceof UpdateMessage) {
                    UpdateMessage updateMsg = (UpdateMessage) obj;
                    //if (updateMsg.?? == null) return;
                    updateMsg.execute(map);

                    //Send back
//                    server.sendToAllTCP(msgWithName);
                    return;
                }
            }

            public void disconnected (Connection c) {
                GameConnection connection = (GameConnection)c;
                if (connection.usrName != null) {

                }
            }
        });

        server.bind(NetWorkCommon.port);
        server.start();
    }

    public static void main(String[] args){
        ServerModel serverModel = new ServerModel() {


            @Override
            public Map getGameMap() {
                return null;
            }

            @Override
            WorldClock getTimer() {
                return null;
            }
        };
    }

}
