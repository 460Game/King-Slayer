import java.io.IOException;


import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;


import java.util.ArrayList;

public class ServerModel {

    // This holds per connection state.
    public static class GameConnection extends Connection {
        public String usrName;
    }

    Server server;
    ArrayList<GameConnection> clients;
    public ServerModel () {
        clients = new ArrayList<>();
        server = new Server() {
            protected Connection newConnection() {
                return new ServerModel.GameConnection();
            }
        };
    }


    public void start() throws IOException {
        NetWorkCommon.register(server);

        server.addListener(new Listener() {
            public void received (Connection c, Object obj) {

                // We know all connections for this server are actually ChatConnections.
                GameConnection connection = (GameConnection)c;
                clients.add(connection);
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
