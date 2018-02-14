package network;


import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.game.model.ClientGameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import javafx.util.Pair;
import lobby.RoleChoice;
import lobby.TeamChoice;

import java.util.*;

public class LobbyServer { //extends Application {

    static {
        Log.set(Log.LEVEL_INFO);
    }


    private ServerGameModel serverModel;
    private RemoteConnection server;
    private Set<RemoteConnection.RemoteModel> remoteModels;

    public Map<Integer, Pair<Team, Role>> conn2TeamAndRole = new HashMap<>();
    public Map<Integer, ClientGameModel> conn2ClientGameModel = new HashMap<>();
    public Map<RemoteConnection.RemoteModel, Pair<Team, Role>> clientGameModelToTeamAndRole;

//    @Override
//    public void start(Stage window) throws Exception {
//        server = new RemoteConnection(true, this, new NetWork2LobbyAdaptor() {
//            @Override
//            public void init() {//should send the map
//                serverModel.init(remoteModels);
//                serverModel.start();
//
//                Log.info(remoteModels.size() + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
//                    Log.info("server start client: " + remoteModel.connectId + "!!!!!!!!!!");
//                    remoteModel.startModel();
//                }
//            }
//
//            @Override
//            public void makeModel() {
//
//            }
//
//            @Override
//            public void getMsg(Message obj) {
//                serverGetMsg(obj);
//            }
//        });
//
//        window.setTitle("Chat Server");
//
//        Button startB = new Button("start button");
//        startB.setOnAction(a -> {
//            startGame();
//        });
//
//        Scene scene = new Scene(startB, 200, 100);
//        window.setScene(scene);
//        window.show();
//    }

    public LobbyServer() {
        conn2TeamAndRole = new HashMap<>();
        conn2ClientGameModel = new HashMap<>();
        clientGameModelToTeamAndRole = new HashMap<>();
    }

    public void setNumOfPlayers(int numOfPlayers) {
        server.setNumOfPlayer(numOfPlayers);
    }

    public void start() throws Exception {
        server = new RemoteConnection(true, this, new NetWork2LobbyAdaptor() {
            @Override
            public void serverInit(Team team, Role role) {//should send the map
                //actually don't use the parameter team and role here

//                startGame();

//                for (Map.Entry<Integer, Pair<Team, Role>> entry : conn2TeamAndRole.entrySet()) {
//                    System.out.println(entry.getValue().getKey() + " " + entry.getValue().getValue());
//                }

                //init the client to team role map
                for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
                    System.out.println("check serverInit: " + remoteModel.getConnectId());
                    System.out.println(conn2TeamAndRole.get(remoteModel.getConnectId()));
                    clientGameModelToTeamAndRole.put(remoteModel,
                            conn2TeamAndRole.get(remoteModel.getConnectId()));
                }


                //TODO: change it to pingback later
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //TODO: put the enum parameters in
                serverModel.init(remoteModels, clientGameModelToTeamAndRole);
                serverModel.start();
                System.out.println("start the server model");


                Log.info(remoteModels.size() + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//                System.exit(0);
                for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
                    Log.info("server start client: " + remoteModel.connectId + "!!!!!!!!!!");
                    remoteModel.startModel();
                }
            }

            @Override
            public void clientInit() {
                //should not use this
            }

            @Override
            public void makeModel() {
                startGame();//server makes model, and ask clients make model here
            }

            @Override
            public void getMsg(Message obj) {
                serverGetMsg(obj);
            }

            @Override
            public void showLobbyTeamChoice() {

            }

            @Override
            public void serverLobbyComfirmTeamAndRole(Integer connId, Team team, Role role) {
                System.out.println("Did put team and role in the map: " + connId);
                conn2TeamAndRole.put(connId, new Pair<>(team, role));
            }

        });

//        window.setTitle("Chat Server");
//

//        Button startB = new Button("start button");
//        startB.setOnAction(a -> {
//            startGame();
//        });
//
//        Scene scene = new Scene(startB, 200, 100);
//        window.setScene(scene);
//        window.show();
    }

//    public static void main(String[] args) throws IOException {
//        Log.set(Log.LEVEL_DEBUG);
//        Application.launch();
//    }

    //TODO implement this
    public void serverGetMsg(Message msg) {
        serverModel.processMessage(msg);
    }

    //start game actually means make model
    public void startGame() {

        remoteModels = server.makeRemoteModel();
        serverModel = new ServerGameModel();

        Log.info("model num: " + remoteModels.size());

        for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
            remoteModel.syncClock();
        }

        Log.info("finish syn!");

        for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
            System.out.println("server wants to start");
            remoteModel.clientMakeModel();
        }

        Log.info("finish make model");
    }
    public void makeServerModel() {}

}

