package network;

import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.game.model.ClientGameModel;
import game.model.game.model.Model;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.view.GameView;
import javafx.application.Platform;
import javafx.stage.Stage;
import lobby.GameView2MainAdaptor;
import lobby.Lobby;
import lobby.PlayerInfo;
import lobby.lobbyMessage.LobbyMessage;
import lobby.Main;

import java.io.IOException;
import java.util.Map;

public class LobbyClient implements Lobby {//extends Application {
    static {
        Log.set(Log.LEVEL_INFO);
    }

    private RemoteConnection client;
    private ClientGameModel clientGameModel;
    private RemoteConnection.RemoteModel serverModel;
    private GameView gameView;
    private Stage window;
    private LobbyClient2LobbyAdaptor lobbyAdaptor;
    public Main mainApp;

    public int connectId;
    public String playerName;

    public LobbyClient(Stage window, LobbyClient2LobbyAdaptor lobbyAdaptor, Main mainApp) {
        this.window = window;
        this.lobbyAdaptor = lobbyAdaptor;
        this.mainApp = mainApp;
    }

    public void lobbyRematch() {
//        clientGameModel = new ClientGameModel(new Model() {
//            @Override
//            public void processMessage(Message m) {
//                if (serverModel != null)
//                    serverModel.processMessage(m);
//                else
//                    Log.info("serverModel null, might happen after game ends");
//            }
//
//            @Override
//            public long nanoTime() {
//                return serverModel.nanoTime();
//            }
//        });
    }

    public void start() throws Exception {

        client = new RemoteConnection(false, new NetWork2LobbyAdaptor() {
            @Override
            public void serverInit() {
                //should not use this
            }

            @Override
            public void clientInit() {
                Log.debug("client init");

                gameView = new GameView(clientGameModel, new GameView2MainAdaptor() {
                    @Override
                    public int closeServer() {
                        return mainApp.closeServer();
                    }

                    @Override
                    public int rematch() {
                        return mainApp.rematch();
                    }

                    @Override
                    public int restartFromMainMenu() {
                        mainApp.restartFromMainMenu();
                        return 0;
                    }
                });

                Platform.runLater(()-> {
                    gameView.start(window);
                });

                Log.debug("Client Started");
            }

            @Override
            public void makeModel() {
                lobbyClientMakeModel();
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                client.notifyMadeModel();
            }

            @Override
            public void getMsg(Message obj) {
                clientGetMsg(obj);
            }

            @Override
            public void showLobbyTeamChoice() {
                Log.info("show lobby team choice");
                lobbyAdaptor.showChoiceTeamAndRoleScene();
            }

            @Override
            public void showLobbyTeamChoice(int numOnTeam) {
                lobbyAdaptor.setNumOnTeam(numOnTeam);
                lobbyAdaptor.showChoiceTeamAndRoleScene();
            }

            @Override
            public void serverLobbyTrySetTeamAndRole(Integer connId, Team team, Role role, String playerName) {
                //client should not call
            }

            @Override
            public void serverStartRematch() {
                //client should not call
            }

            @Override
            public void clientTakeSelectFb(boolean s, Map<Integer, PlayerInfo> map) {
                lobbyAdaptor.takeSelectFb(s, map);
            }

            @Override
            public int getNumOnTeam() {
                System.err.println("not used by client");
                //not used by client
                return -1;
            }

            @Override
            public Map<Integer, PlayerInfo> getPlayerInfoMap() {
                System.out.println("getPlayerInfoMap should not be used here");
                return null;
            }

        });
    }

    public void connectTo(String host) {
        // We'll do the connect on a new thread so the ChatFrame can show a progress bar.
        // Connecting to localhost is usually so fast you won't see the progress bar.
        client.connectToServer(5000, host);
        connectId = client.client.getID();
    }


    public void clientGetMsg(Message msg) {
        if (msg == null) {
            Log.error("msg null!");
            return;
        }
        if (clientGameModel == null) {
            return;
        }
        clientGameModel.processMessage(msg);
    }

    public void lobbyClientReady(Team team, Role role, String name) {
        client.notifyReady(team, role, this.playerName);
    }

    public void lobbyClientReady(String name) {
        client.notifyReady(name);
    }

    //TODO rename this to makeModel
    public void lobbyClientMakeModel() {
        //TODO kinda unsafe here. server might not have a model yet
        serverModel = client.makeRemoteModel().iterator().next();
        Log.info("lobby client has made the server model");

        //TODO !!!! don't have getGenerator
        clientGameModel = new ClientGameModel(new Model() {
            @Override
            public void processMessage(Message m) {
                if (serverModel != null)
                    serverModel.processMessage(m);
                else
                    Log.info("serverModel null, might happen after game ends");
            }

            @Override
            public long nanoTime() {
                return serverModel.nanoTime();
            }
        });
    }

    public int restartFromReadyPage() {
        //nop for now
        clientGameModel.stop();
        gameView.stop();
        gameView = null;
        clientGameModel = null;

        serverModel = null;
        int status = client.restartFromReadyPage();
        return status;
    }

    public void stop() {
        if (gameView != null)
            gameView.stop();
        gameView = null;
        if (clientGameModel != null)
            clientGameModel.stop();
        clientGameModel = null;

        client.client.stop();
    }

    @Override
    public void processMessage(LobbyMessage msg) {
        msg.execuate(this);
    }

    public int closeClientLobby() {
        if (client != null) {
            client.stop();
            client = null;
        }
        //clientGameModel does not have a close method
//        if (clientGameModel != null) {
//            clientGameModel.stop();
//        }
        return 0;
    }

    public void lobbyClientRematch() {
        gameView.stop();
        gameView = null;
        clientGameModel.stop();

        clientGameModel = null;
        clientGameModel = new ClientGameModel(new Model() {
            @Override
            public void processMessage(Message m) {
                if (serverModel != null)
                    serverModel.processMessage(m);
                else
                    Log.info("serverModel null, might happen after game ends");
            }

            @Override
            public long nanoTime() {
                return serverModel.nanoTime();
            }
        });

        client.rematch();
    }

    public void setName(String name) {
        playerName = name;
    }

    public void selectRole(Team team, Role role) {
        client.trySelectRole(team, role, playerName);
    }
}

