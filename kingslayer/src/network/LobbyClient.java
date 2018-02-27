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
import lobby.Lobby;
import lobby.lobbyMessage.LobbyMessage;
import lobby.Main;

import java.io.IOException;

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

    public LobbyClient(Stage window, LobbyClient2LobbyAdaptor lobbyAdaptor, Main mainApp) {
        this.window = window;
        this.lobbyAdaptor = lobbyAdaptor;
        this.mainApp = mainApp;
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

                gameView = new GameView(clientGameModel, mainApp);
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
            public void serverLobbyComfirmTeamAndRole(Integer connId, Team team, Role role, String playerName) {
                //client should not call
            }

        });
    }

    public void connectTo(String host) throws IOException {
        // We'll do the connect on a new thread so the ChatFrame can show a progress bar.
        // Connecting to localhost is usually so fast you won't see the progress bar.
        client.connectToServer(5000, host);
        connectId = client.client.getID();
    }


    public void clientGetMsg(Message msg) {
        if (msg == null) Log.error("msg null!");
        if (clientGameModel == null) Log.error("clientGameModel is null!");
        clientGameModel.processMessage(msg);
    }

    public void lobbyClientReady(Team team, Role role, String playerName) {
        client.notifyReady(team, role, playerName);
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
                serverModel.processMessage(m);
            }

            @Override
            public long nanoTime() {
                return serverModel.nanoTime();
            }
        });
    }

    public int restartFromReadyPage() {
        //nop for now
        gameView = null;

        clientGameModel = null;
        serverModel = null;
        int status = client.restartFromReadyPage();
        System.out.println("want it to be null: " + gameView + " status: " + status);
        return status;
    }

    @Override
    public void processMessage(LobbyMessage msg) {
        msg.execuate(this);
    }
}

