package game.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.kryonet.Server;
import game.message.Message;

public class RemoteConnection {
    boolean isServer;
    Server server;
    Client client;
    public RemoteConnection(boolean isServer) {
        if (isServer) {
            server = new Server() {};
        } else {
//            client = (Client) connection;
        }
    }
    public void send(Message msg) {
        if (isServer) {
            server.sendToAllTCP(msg);
        }
    }
}

