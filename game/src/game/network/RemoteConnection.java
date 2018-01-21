package game.network;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.Game.Model.Model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * For client => construct => start => connectToServer, start game: makeRemoteModels =>
 */
public class RemoteConnection {
    // This holds per connection state.
    public static class GameConnection extends Connection {
        public String usrName;
        public GameConnection(String name) {
            usrName = name;
        }
    }

    static final int TOTALPLAYER = 2;

    boolean isServer;
    NetWork2LobbyAdaptor adaptor;

    Server server;
    ConcurrentHashMap<Integer, GameConnection> clientList;
    int readyClient = 0;

    Client client;

//    GameConnection serverConnectionForClient;



    public RemoteConnection(boolean isServer, Object lobby, NetWork2LobbyAdaptor adaptor) throws IOException {
        this.isServer = isServer;
        this.adaptor = adaptor;
        if (isServer) {
//            //TODO change this later
//            lobbyServer = (LobbyServer) lobby;
            clientList = new ConcurrentHashMap<>();
            server = new Server() {
                protected Connection newConnection() {
                    return new RemoteConnection.GameConnection("ServerName");
                }
            };
        } else {
//            //TODO change this later
//            lobbyClient = (LobbyClient) lobby;
            client = new Client();
        }
        start();
    }

    public void start() throws IOException {
        if (isServer) {
            NetworkCommon.register(server);

            server.addListener(new Listener() {
                public void received (Connection c, Object obj) {
                    Log.info("Server received from " + c.getID() + " " + obj.toString());
                    GameConnection connection = (GameConnection)c;

                    clientList.putIfAbsent(connection.getID(), connection);

                    if (obj instanceof NetworkCommon.ClientReadyMsg) {
                        readyClient++;
                        Log.info("!!!!!!!!! " + readyClient + " are ready !!!!!!!");
                        if (readyClient == TOTALPLAYER) {
                            adaptor.init();//send the map
                        }
                    }
                    if (obj instanceof Message) {
//                        lobbyServer.getMsg((Message) obj);
                        adaptor.getMsg((Message) obj);
                    }

                }

                //TODO: implement disconnected
                public void disconnected (Connection c) {

                }
            });

            server.bind(NetworkCommon.port);
            server.start();
            Log.info("Server started");
        } else {
            NetworkCommon.register(client);
            client.start();



            client.addListener(new Listener() {
                public void connected (Connection connection) {
                    Log.info("Client " + connection.getID() + " connected");
                    client.sendTCP("Now server should see this");
//                    NetworkCommon.UserName usrName = new NetworkCommon.UserName("");
//                    usrName.user_name = name;
//                    client.sendTCP(usrName);
                }

                public void received (Connection connection, Object obj) {
                    Log.info("Client " + client.getID() + "received " + obj.toString());
                    if (obj instanceof NetworkCommon.ClientMakeModelMsg) {
//                        lobbyClient.startGame();
                        adaptor.makeModel(); //make clientModel
                        client.sendTCP("A client is ready");
                        client.sendTCP(new NetworkCommon.ClientReadyMsg());
                    }

                    if (obj instanceof NetworkCommon.ClientStartModelMsg) {
                        adaptor.init();
                    }

                    if (obj instanceof Message) {
//                        lobbyClient.getMsg((Message) obj);
                        adaptor.getMsg((Message) obj);
                    }

//                    if (object instanceof ChatCommon.AllUserNames) {
//                        ChatCommon.AllUserNames updateNames = (ChatCommon.AllUserNames) object;
//                        chatFrame.setNames(updateNames.names);
//                        return;
//                    }
//
//                    if (object instanceof ChatCommon.ChatMsg && !alreadyPingBack) {
//                        endTime = System.nanoTime();
//                        Log.info("Time: " + (endTime - startTime));
//                        ChatCommon.ChatMsg chatMessage = (ChatCommon.ChatMsg) object;
//                        chatFrame.addMessage(chatMessage.msg);
//                        return;
//                    }
                }

                public void disconnected (Connection connection) {
                    Log.info("Disconnected");
                }
            });
        }
    }

    /**
     *
     * @param port
     * @param host
     * @throws IOException
     */
    public void connectToServer(int port, String host) throws IOException {
        if (isServer) {
            Log.error("Server should not connect to server crossing network");
            return;
        }
        Log.info("Client connect to " + host + " " + port);
        new Thread("Connect") {
            public void run () {
                try {
                    client.connect(port, host, NetworkCommon.port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Server communication after connection can go here, or in Listener#connected().
            }
        }.start();

    }

    public void stop() {
        if (isServer) server.stop();
        else client.stop();
    }

    public Set<RemoteModel> makeRemoteModel() {
        Set<RemoteModel> remoteModels = new HashSet<>();
        if (isServer) {
            for (int id : clientList.keySet()) {
                remoteModels.add(new RemoteModel(id));
            }
        } else {
            remoteModels.add(new RemoteModel(-1));//not using the conid
        }
        return remoteModels;
    }

    class RemoteModel implements Model {
        int connectId;
        public RemoteModel(int conid) {
            connectId = conid;
        }

        @Override
        public void processMessage(Message m) {
            if (isServer) { //then the remote is a client
                server.sendToTCP(connectId, m);
            } else {
//                Log.info(m.toString());
                client.sendTCP("client want to send a msg");
                client.sendTCP(m);
            }
        }

        @Override
        public long nanoTime() {
            return System.nanoTime();
        } //TODO this wont work across computers

        public int getConnectId() {
            if (isServer) return -1;
            return connectId;
        }

        public void startGame() {
            if (isServer) { // means it is server call this method on remote model
                Log.info("Server click start the game");
                server.sendToAllTCP(new NetworkCommon.ClientMakeModelMsg());
            }
            else {// a client should not make server to start
                Log.error("ERROR: client start the game");
            }
        }

        public void notifyReady() {
            if (!isServer) {
                Log.info("Client is ready");
                client.sendTCP(new NetworkCommon.ClientReadyMsg());
            } else {
                Log.error("ERROR: Server should not call notifyReady");
            }
        }

        //call by server to ask the client to start the model
        public void startModel() {
            if (!isServer) return;
            server.sendToAllTCP(new NetworkCommon.ClientStartModelMsg());
        }
    }


//    public void send(Message msg) {
//        if (isServer) {
//            server.sendToAllTCP(msg);
//        }
//    }
}

