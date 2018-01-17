package game.network;

import java.io.IOException;


import Util.Util;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import game.message.Message;
import game.model.Game.GameModel;
import game.model.IModel;
import game.model.RemoteModel;
import game.model.ServerGameModel;
import game.view.AIView;
import game.view.ClientView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LobbyServer {

    private GameModel serverModel;

    // This holds per connection state.
    public static class GameConnection extends Connection {
        public String usrName;
    }

    Server server;
    ArrayList<GameConnection> clients;
    Map<GameConnection, IModel> map;
    public LobbyServer() {
//        clients = new ArrayList<>();
//        map = new HashMap<>();
//
//        server = new Server() {
//            protected Connection newConnection() {
//                return new LobbyServer.GameConnection();
//            }
//        };
    }


    public void start() throws IOException {
//        NetworkCommon.register(server);
//
//        server.addListener(new Listener() {
//            public void received (Connection c, Object obj) {
//
//                // We know all connections for this server are actually ChatConnections.
//                GameConnection connection = (GameConnection)c;
//                clients.add(connection);
//
//                if (clients.size() >= 2) {
//                    startGame();
//                }
//            }
//
//            //TODO: implement disconnected
//            public void disconnected (Connection c) {
//                GameConnection connection = (GameConnection)c;
//                if (connection.usrName != null) {
//
//                }
//            }
//        });
//
//        server.bind(NetworkCommon.port);
//        server.start();
    }

    public static void main(String[] args) throws IOException {
//        LobbyServer serverModel = new LobbyServer();
//        serverModel.start();
    }

    public void startGame() {
//        for (GameConnection gc: clients) {
//            map.put(gc, new GameModel());
//        }
//        serverModel = new ServerGameModel(Collections.singleton(new IModel() {
//
//            @Override
//            public void processMessage(Message m) {
//                Message copy = Util.KYRO.copy(m); //to simulate going thoruhg network - make a copy
//                clientModel.processMessage(copy);
//            }
//
//            @Override
//            public long nanoTime() {
//                return clientModel.nanoTime();
//            }
//        }));
    }

}

