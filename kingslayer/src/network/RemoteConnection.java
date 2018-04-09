package network;

import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.game.model.*;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import lobby.PlayerInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * For client => construct => start => connectToServer, start game: makeRemoteModels =>
 */
public class RemoteConnection {
    static {
        Log.set(Log.LEVEL_INFO);
    }

    private boolean running = false;
    private ConcurrentHashMap rematchClients = new ConcurrentHashMap();

    public void notifyMadeModel() {
        if (isServer) return;//server should not use this
        client.sendTCP(new NetworkCommon.ClientFinishMakingModelMsg());
    }

    public int rematch() {
        if (isServer) {
            running = false;

            toBeConsumeMsgQueue = new LinkedBlockingQueue<>();
            sendQueueMsgThread = new Thread(this::sendQueueMsg, sendQMark + " Send Batched Message Thread");
            consumeQueueMsgThread = new Thread(this::consumeReceivedMsg, consumeQMark + " Consume Msg Thread");

            for (Integer clientId : messageQueues.keySet()) {
                messageQueues.put(Integer.valueOf(clientId), new LinkedBlockingQueue<>());
            }

            running = true;
            sendQueueMsgThread.start();
            consumeQueueMsgThread.start();

            server.sendToAllTCP(new NetworkCommon.ClientStartModelMsg());
            return 0;
        }
        else {

            running = false;

            while (!sendQueueMsgThread.isInterrupted()) sendQueueMsgThread.interrupt();
            while (!consumeQueueMsgThread.isInterrupted()) consumeQueueMsgThread.interrupt();

            sendQueueMsgThread = new Thread(this::sendQueueMsg, sendQMark + " Send Batched Message Thread");
            consumeQueueMsgThread = new Thread(this::consumeReceivedMsg, consumeQMark + " Consume Msg Thread");


            for (Integer id : messageQueues.keySet()) {
                messageQueues.remove(Integer.valueOf(id));
                messageQueues.put(Integer.valueOf(id), new LinkedBlockingQueue<>());
            }

            toBeConsumeMsgQueue = new LinkedBlockingQueue<>();

            running = true;
            sendQueueMsgThread.start();
            consumeQueueMsgThread.start();

            client.sendTCP(new NetworkCommon.ClientRematchMsg());
            Log.info("client sent rematch message");
            return 0;
        }
    }

    public void trySelectRole(Team team, Role role, String playerName) {
        if (isServer) return;
        client.sendTCP(new NetworkCommon.SelectRoleMsg(team, role, playerName));
    }

    public void confirmSelect(boolean success, Map<Integer, PlayerInfo> selectResult) {
        server.sendToAllTCP(new NetworkCommon.SelectFeedBackMsg(success, selectResult));
    }


    // This holds per connection state.
    public static class GameConnection extends Connection {
        public String usrName;
        public GameConnection(String name) {
            usrName = name;
        }
    }

    volatile boolean isServer;
    NetWork2LobbyAdaptor adaptor;

    Server server;
    ConcurrentHashMap<Integer, GameConnection> clientList;
    ConcurrentHashMap<Integer, GameConnection> restartClientList;
    ConcurrentHashMap<Integer, GameConnection> readyClients;
    int cntClientModelsMade = 0;


    Client client;

    long latencty = 0;
    Long clientStartTime = null;
    Long serverStartTime = null;
    Long t0 = null;
    Long t1 = null;

    int numOfPlayer = 1;
    int consumeQMark;
    int sendQMark;

    Map<Integer, LinkedBlockingQueue<Message>> messageQueues;
    LinkedBlockingQueue<Message> toBeConsumeMsgQueue;
    Set<RemoteModel> remoteModels;

    long delta = (long) 10E6;

    volatile Thread sendQueueMsgThread;
    volatile Thread consumeQueueMsgThread;

    public int restartFromReadyPage() {
        remoteModels = null;
        if (isServer) {
            System.out.println("clear and send all client connect");
            readyClients = new ConcurrentHashMap<>();
            cntClientModelsMade = 0;

            rematchClients = new ConcurrentHashMap();

            running = false;

            consumeQMark = new Random().nextInt(100);
            sendQMark = new Random().nextInt(100);

//            while (!sendQueueMsgThread.isInterrupted()) sendQueueMsgThread.interrupt();
//            while (!consumeQueueMsgThread.isInterrupted()) consumeQueueMsgThread.interrupt();

            toBeConsumeMsgQueue = new LinkedBlockingQueue<>();
            sendQueueMsgThread = new Thread(this::sendQueueMsg, sendQMark + " Send Batched Message Thread");
            consumeQueueMsgThread = new Thread(this::consumeReceivedMsg, consumeQMark + " Consume Msg Thread");



            for (Integer clientId : messageQueues.keySet()) {
                messageQueues.put(Integer.valueOf(clientId), new LinkedBlockingQueue<>());
            }

//            toBeConsumeMsgQueue = new LinkedBlockingQueue<>();
//            sendQueueMsgThread = new Thread(this::sendQueueMsg, sendQMark + " Send Batched Message Thread");
//            consumeQueueMsgThread = new Thread(this::consumeReceivedMsg, consumeQMark + " Consume Msg Thread");


            running = true;
            sendQueueMsgThread.start();
            consumeQueueMsgThread.start();

//            server.sendToAllTCP(new NetworkCommon.AllClientConnectMsg());
            return 0;
        }
        else {

            running = false;

            consumeQMark = new Random().nextInt(100);
            sendQMark = new Random().nextInt(100);

            while (!sendQueueMsgThread.isInterrupted()) sendQueueMsgThread.interrupt();
            while (!consumeQueueMsgThread.isInterrupted()) consumeQueueMsgThread.interrupt();

            sendQueueMsgThread = new Thread(this::sendQueueMsg, sendQMark + " Send Batched Message Thread");
            consumeQueueMsgThread = new Thread(this::consumeReceivedMsg, consumeQMark + " Consume Msg Thread");


            for (Integer id : messageQueues.keySet()) {
                messageQueues.remove(Integer.valueOf(id));
                messageQueues.put(Integer.valueOf(id), new LinkedBlockingQueue<>());
            }

            toBeConsumeMsgQueue = new LinkedBlockingQueue<>();

            running = true;
            sendQueueMsgThread.start();
            consumeQueueMsgThread.start();


            client.sendTCP(new NetworkCommon.ClientRestartMsg());
            Log.info("client sent restart message");
            return 0;
        }
    }

    public void setNumOfPlayer(int num) {
        numOfPlayer = num;
    }

    public RemoteConnection(boolean isServer, NetWork2LobbyAdaptor adaptor) throws IOException {
        Log.set(Log.LEVEL_INFO);
        this.isServer = isServer;
        this.adaptor = adaptor;

        messageQueues = new HashMap<>();
        toBeConsumeMsgQueue = new LinkedBlockingQueue<>();


        if (isServer) {
//            //TODO change this later

            clientList = new ConcurrentHashMap<>();
            restartClientList = new ConcurrentHashMap<>();
            readyClients = new ConcurrentHashMap<>();

            server = new Server(10000000, 10000000 /2) {
                protected Connection newConnection() {
                    return new RemoteConnection.GameConnection("ServerName");
                }
            };
//            System.out.println("!!!!!!! " + InetAddress.getLocalHost() + " !!!!!!!!");
        } else {
//            //TODO change this later
            client = new Client(10000000, 10000000 /2);
        }
        setKyroServerAndClient();
        start();
    }

    public void setKyroServerAndClient() throws IOException {
        if (isServer) {//is server receives it
            NetworkCommon.register(server);

            server.addListener(new Listener() {
                public void received (Connection c, Object obj) {
               //     Log.info("Server received from " + c.getID() + " " + obj.toString());
                    GameConnection connection = (GameConnection)c;

                    //init a queue when have a new client
                    if (!clientList.containsKey(connection.getID())) {
                        System.out.println("still find a new client");
                        clientList.putIfAbsent(connection.getID(), connection);
                        messageQueues.put(connection.getID(), new LinkedBlockingQueue<>());

                        if (clientList.size() == numOfPlayer) {//all clients connected
                            server.sendToAllTCP(new NetworkCommon.AllClientConnectMsg());
//                            adaptor.makeModel();
                        }
                    }

                    if (obj instanceof NetworkCommon.ClientRematchMsg) {
                        rematchClients.put(connection.getID(), connection);
                        System.out.print("rematch size: " + rematchClients.size());
                        System.out.println(" while clientList size is " + clientList.size());
                        if (rematchClients.size() == clientList.size()) {
//                            RemoteConnection.this.rematch();
                            adaptor.serverStartRematch();
                            rematchClients = new ConcurrentHashMap();
                        }
                    }

                    if (obj instanceof NetworkCommon.ClientFinishMakingModelMsg) {
                        cntClientModelsMade++;
                        if (cntClientModelsMade == clientList.size()) {
                            System.out.println(cntClientModelsMade + " client models are made, start");
                            adaptor.serverInit();
                        }
                    }

                    if (obj instanceof NetworkCommon.ClientRestartMsg) {
                        restartClientList.put(connection.getID(), connection);

                        Log.info("restart number: " + restartClientList.size());

                        if (restartClientList.size() == clientList.size()) {
                            System.out.println("begin restart");
                            server.sendToAllTCP(new NetworkCommon.AllClientConnectMsg());

                            restartClientList.clear();
                        }
                    }

                    if (obj instanceof NetworkCommon.ClientReadyMsg) {

                        // Note: readyMsg.getTeam and getRole are not used anymore!
                        NetworkCommon.ClientReadyMsg readyMsg = (NetworkCommon.ClientReadyMsg) obj;

                        if (readyClients.containsKey(connection.getID())) {
                            return;
                        }

                        if (!adaptor.getPlayerInfoMap().containsKey(connection.getID())) {
                            System.err.println("can't ready without role, team!");
                            server.sendToTCP(c.getID(), new NetworkCommon.ReadyStatusMsg(false));
                            return;
                        } else {
                            server.sendToTCP(c.getID(), new NetworkCommon.ReadyStatusMsg(true));
                            server.sendToAllTCP(new NetworkCommon.ReadyLockMsg(adaptor.getPlayerInfoMap().get(c.getID())));
                        }

                        System.out.println("checccccc: " + readyMsg.getTeam() + readyMsg.getRole() + readyMsg.getPlayerName());
//                        adaptor.serverLobbyTrySetTeamAndRole(connection.getID(),
//                                readyMsg.getTeam(), readyMsg.getRole(), readyMsg.getPlayerName());



                        readyClients.put(connection.getID(), connection);
                        System.out.println("READY CLIENTS: " + readyClients.size());

                        if (readyClients.size() == clientList.size()) {
                            //works fine here
                            adaptor.makeModel();//server make model and tells client make models
                            //also need to start game (make model)
                        }
                    }

                    if (obj instanceof ArrayList) {
                        for (Message msg : (ArrayList<Message>) obj) {
                            adaptor.getMsg(msg);
                        }
                    }

                    if (obj instanceof Message) {
                        adaptor.getMsg((Message) obj);
                    }

                    if (obj instanceof NetworkCommon.RequestSessionPlayerInfo) {
                        NetworkCommon.RequestSessionPlayerInfo msg = (NetworkCommon.RequestSessionPlayerInfo) obj;
                        server.sendToTCP(c.getID(), new NetworkCommon.SessionPlayerInfoCmd(adaptor.getNumOnTeam()));
                    }

                    if (obj instanceof NetworkCommon.SyncClockMsg) {
//                        long guessServerTime = ((NetworkCommon.SyncClockMsg) obj).getServerTime();
//                        long curTime = System.nanoTime();
//                        if (Math.abs(guessServerTime - curTime) > delta) {
//                            server.sendToTCP(c.getID(), new NetworkCommon.SyncClockMsg(curTime));
//                        }
                        server.sendToTCP(c.getID(), new NetworkCommon.SyncClockMsg(System.nanoTime()));
//                        Log.info(curTime + " " + guessServerTime);
                    }

                    if (obj instanceof NetworkCommon.SelectRoleMsg) {
                        //cannot change role or team if ready!!!
                        if (readyClients.containsKey(c.getID())) return;
                        NetworkCommon.SelectRoleMsg msg = (NetworkCommon.SelectRoleMsg) obj;
                        adaptor.serverLobbyTrySetTeamAndRole(c.getID(), msg.tryTeam, msg.tryRole, msg.name);
                    }

                }

                //TODO: implement disconnected
                public void disconnected (Connection c) {
                    server.stop();
                }
            });

            server.bind(NetworkCommon.port);
            server.start();
            Log.debug("Server started");
        } else {//if the client receives it
            NetworkCommon.register(client);
            client.start();
            client.addListener(new Listener() {
                public void connected (Connection connection) {
                    Log.info("Client " + connection.getID() + " connected");
                    client.sendTCP("Client " + connection.getID() + " connected");
                    //use client ID for the queue for client use
                    messageQueues.put(client.getID(), new LinkedBlockingQueue<>());

                    client.sendTCP(new NetworkCommon.RequestSessionPlayerInfo());
                }

                public void received (Connection connection, Object obj) {


                    Log.debug("Client " + client.getID() + "received " + obj.toString());

                    if (obj instanceof NetworkCommon.ReadyLockMsg) {
                        adaptor.roleReadLock(((NetworkCommon.ReadyLockMsg)obj).info);
                    }
                    if (obj instanceof NetworkCommon.ReadyStatusMsg) {
                        adaptor.readyButtonFb(((NetworkCommon.ReadyStatusMsg) obj).status);
                    }
                    if (obj instanceof NetworkCommon.SessionPlayerInfoCmd) {
                        NetworkCommon.SessionPlayerInfoCmd msg = (NetworkCommon.SessionPlayerInfoCmd) obj;
                        adaptor.showLobbyTeamChoice(msg.num);
                        client.sendTCP(new NetworkCommon.SelectRoleMsg(null, null, null));
                    }

                    if (obj instanceof NetworkCommon.SelectFeedBackMsg) {
                        NetworkCommon.SelectFeedBackMsg msg = (NetworkCommon.SelectFeedBackMsg) obj;
                        adaptor.clientTakeSelectFb(msg.s, msg.map);
                    }
                    if (obj instanceof NetworkCommon.ClientMakeModelMsg) {
                        adaptor.makeModel(); //make clientModel
//                        client.sendTCP(new NetworkCommon.ClientReadyMsg()); //trigger by sth else now
                    }

                    if (obj instanceof NetworkCommon.ClientStartModelMsg) {
                        adaptor.clientInit();
                    }

                    if (obj instanceof NetworkCommon.AllClientConnectMsg) {
                        Log.info(connection.getID() + "get all client connectMsg");
//                        adaptor.showLobbyTeamChoice();
                    }

                    if (obj instanceof ArrayList) {
                        //enqueue the message
                        toBeConsumeMsgQueue.addAll((Collection<? extends Message>) obj);
                    }

                    if (obj instanceof Message) {
                        adaptor.getMsg((Message) obj);
                    }

                    if (obj instanceof NetworkCommon.SyncClockMsg) {
                        if (t1 != null && t0 != null) return;//restart do not need sync
                        System.out.println("Sync message");
                        if (clientStartTime == null) {
                            clientStartTime = System.nanoTime();
                            serverStartTime = ((NetworkCommon.SyncClockMsg) obj).getServerTime();
                            client.sendTCP(new NetworkCommon.SyncClockMsg(serverStartTime));//what client think the server time is
                            Log.info("" + ((System.nanoTime() - clientStartTime + latencty) + serverStartTime));
                            t0 = clientStartTime;
                        }

                        else {
                            t1 = System.nanoTime();
                            latencty = (t1 - t0) / 2;
                        }
                    }

                }

                public void disconnected (Connection connection) {
                    Log.info("Disconnected");
                }
            });
        }
    }

    public void start() throws IOException {


        consumeQMark = new Random().nextInt(100);
        sendQMark = new Random().nextInt(100);
        sendQueueMsgThread = new Thread(this::sendQueueMsg, sendQMark + " Send Batched Message Thread");
        consumeQueueMsgThread = new Thread(this::consumeReceivedMsg, consumeQMark + " Consume Msg Thread");
        running = true;
        sendQueueMsgThread.start();
        consumeQueueMsgThread.start();
    }

    private void consumeReceivedMsg() {
        while (running && currentThread() == consumeQueueMsgThread) {
            try {
                sleep(10);
            } catch (InterruptedException e) {
//                e.printStackTrace();
                currentThread().interrupt();
            }

//            System.err.println(consumeQMark + " running consume Q " + currentThread() + " " + consumeQueueMsgThread);
            while (!toBeConsumeMsgQueue.isEmpty()) {
                adaptor.getMsg(toBeConsumeMsgQueue.poll());
            }
        }
    }

    private void sendQueueMsg() {
        while (running && currentThread() == sendQueueMsgThread) {

            try {
                sleep(5);
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
//            System.err.println(sendQMark + " running send Q " + currentThread() + " " + sendQueueMsgThread);
            if (isServer) {
                for (int connectId : messageQueues.keySet()) {
                    List<Message> messageList = new ArrayList<>();
                    LinkedBlockingQueue q = messageQueues.get(connectId);
                    q.drainTo(messageList);
                    if (messageList.size() <= 0) continue;
                    server.sendToTCP(connectId, messageList);
                }
            } else {
                if (messageQueues.size() < 1) continue;
                List<Message> messageList = new ArrayList<>();
                LinkedBlockingQueue q = messageQueues.get(client.getID());
                q.drainTo(messageList);
                if (messageList.size() <= 0) continue;
                client.sendTCP(messageList);
            }
        }
    }

    private void enqueueMsg(Message msg, int conId) {
        if (isServer)
            messageQueues.get(conId).add(msg);
        else
            messageQueues.get(client.getID()).add(msg);
    }

    /**
     *
     * @param port
     * @param host
     * @throws IOException
     */
    public int connectToServer(int port, String host) {
        if (isServer) {
            Log.error("Server should not connect to server crossing network");
            return -1;
        }
        Log.info("Client connect to " + host + " " + port);
//        new Thread("Connect") {
//            public void run () {
//                try {
//                    client.connect(port, host, NetworkCommon.port);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                // Server communication after connection can go here, or in Listener#connected().
//            }
//        }.start();
        try {
            client.connect(port, host, NetworkCommon.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int stop() {
        readyClients = null;
        cntClientModelsMade = 0;

        running = false;

        sendQueueMsgThread = null;
        consumeQueueMsgThread = null;
//        toBeConsumeMsgQueue = null;

        if (isServer) {
            server.stop();
        }

        else {
            client.stop();
        }

        return 0;
    }

    public void notifyReady(Team team, Role role, String playerName) {
        if (isServer) System.err.println("Server should not do this");
        else client.sendTCP(new NetworkCommon.ClientReadyMsg(team, role, playerName));
    }

    public void notifyReady(String playerName) {
        if (isServer) System.err.println("Server should not do this");
        else client.sendTCP(new NetworkCommon.ClientReadyMsg(null, null, playerName));
    }

    //if isServer, make client remoteModel
    public Set<RemoteModel> makeRemoteModel() {
//        if (remoteModels != null) return remoteModels;
        remoteModels = new HashSet<>();
        if (isServer) {
            System.out.println("Server make remote model");
            for (int id : clientList.keySet()) {
//                System.out.println("server remote model id: " + id);
                remoteModels.add(new RemoteModel(id));
            }
        } else {
            System.out.println("Client make remote model");
            remoteModels.add(new RemoteModel(-1));//not using the conid
        }
        return remoteModels;
    }

    class RemoteModel implements Model {
        int connectId;

        String playerName;

        Team myTeam;

        Role myRole;

        public Team getTeam() {
            return myTeam;
        }

        public Role getRole() {
            return myRole;
        }

        public String getPlayerName() {
            return playerName;
        }

        public void setTeam(Team team) {
            myTeam = team;
        }

        public void setPlayerName(String p) {
            playerName = p;
        }

        public void setRole(Role role) {
            myRole = role;
        }

        public RemoteModel(int conid) {
            connectId = conid;
        }

        @Override
        public void processMessage(Message m) {
            if (isServer) { //then the remote is a client
                enqueueMsg(m, connectId);
            } else {
                enqueueMsg(m, connectId);
            }
        }

        @Override
        public long nanoTime() {
            //clientStartTime should actually start before the received startTime, so plus the latency
            return (System.nanoTime() - clientStartTime + latencty) + serverStartTime;
        }

        /**
         *
         * @return the connect id of this connection
         */
        public int getConnectId() {//TODO: need to double what connect id means
//            if (isServer) return -1;
//            return connectId;
            return connectId;
        }

        public void clientMakeModel() {
            if (isServer) { // means it is server call this method on remote model
                Log.debug("Server click start the game");
                server.sendToTCP(connectId, new NetworkCommon.ClientMakeModelMsg());
            }
            else {// a client should not make server to start
                Log.error("ERROR: client start the game");
            }
        }

        //call by server to ask the client to start the model
        public void startModel() {
            if (!isServer) return;
            server.sendToTCP(connectId, new NetworkCommon.ClientStartModelMsg());
        }

        public void syncClock() {
            if (isServer) server.sendToAllTCP(new NetworkCommon.SyncClockMsg(System.nanoTime()));

        }
    }

}

