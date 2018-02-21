package network;


import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.game.model.ClientGameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import javafx.util.Pair;
import lobby.Lobby;
import lobby.lobbyMessage.LobbyMessage;
import lobby.PlayerInfo;

import java.util.*;

public class LobbyServer implements Lobby { //extends Application {

    static {
        Log.set(Log.LEVEL_INFO);
    }


    private ServerGameModel serverModel;
    private RemoteConnection server;
    private Set<RemoteConnection.RemoteModel> remoteModels;

    public Map<Integer, Pair<Team, Role>> conn2TeamAndRole = new HashMap<>();
    public Map<Integer, String> conn2PlayerName = new HashMap<>();

    public Map<Integer, PlayerInfo> conn2PlayerInfo = new HashMap<>();
    public Map<RemoteConnection.RemoteModel, PlayerInfo> clientGameModelToPlayerInfo;

    public Map<Integer, ClientGameModel> conn2ClientGameModel = new HashMap<>();
    public Map<RemoteConnection.RemoteModel, Pair<Team, Role>> clientGameModelToTeamAndRole;

    public LobbyServer() {
        conn2TeamAndRole = new HashMap<>();
        conn2ClientGameModel = new HashMap<>();
        clientGameModelToTeamAndRole = new HashMap<>();
    }

    public void setNumOfPlayers(int numOfPlayers) {
        server.setNumOfPlayer(numOfPlayers);
    }

    public void start() throws Exception {
        server = new RemoteConnection(true, new NetWork2LobbyAdaptor() {
            @Override
            public void serverInit() {//should send the map

                //init the client to team role map
                for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
                    System.out.println("check serverInit: " + remoteModel.getConnectId());
                    clientGameModelToTeamAndRole.put(remoteModel,
                            conn2TeamAndRole.get(remoteModel.getConnectId()));
                    clientGameModelToPlayerInfo.put(remoteModel, conn2PlayerInfo.get(remoteModel.getConnectId()));
                }


                //TODO: change it to pingback later
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //TODO: put the enum parameters in
//                serverModel.init(remoteModels, clientGameModelToTeamAndRole);
                serverModel.init(remoteModels, clientGameModelToPlayerInfo);
                serverModel.start();

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
            public void serverLobbyComfirmTeamAndRole(Integer connId, Team team, Role role, String playerName) {
                System.out.println("Did put team and role in the map: " + connId);
                conn2TeamAndRole.put(connId, new Pair<>(team, role));
                conn2PlayerName.put(connId, playerName);
                conn2PlayerInfo.put(connId, new PlayerInfo(team, role, playerName));
            }

        });
    }


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

    public int restartFromReadyPage() {
        conn2TeamAndRole = new HashMap<>();
        conn2ClientGameModel = new HashMap<>();
        clientGameModelToTeamAndRole = new HashMap<>();
        serverModel.stop();

        remoteModels = null;
        serverModel = null;
        int status = server.restartFromReadyPage();
        return status;
    }

    public void closeServer() {
        server.server.stop();
//        server.server.close();
    }

    @Override
    public void processMessage(LobbyMessage msg) {
        msg.execuate(this);
    }

//    public void validateTeamRolePlayer(PlayerInfo playerInfo) {
//
//    }
}

