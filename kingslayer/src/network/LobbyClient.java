package network;

import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.game.model.ClientGameModel;
import game.model.game.model.Model;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.view.GameView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lobby.Main;
import lobby.RoleChoice;
import lobby.TeamChoice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LobbyClient {//extends Application {
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
            public void serverLobbyComfirmTeamAndRole(Integer connId, Team team, Role role) {
                //client should not call
            }

        });
    }

    public void connectTo(String host) throws IOException {
        // We'll do the connect on a new thread so the ChatFrame can show a progress bar.
        // Connecting to localhost is usually so fast you won't see the progress bar.
        client.connectToServer(5000, host);
    }


    public void clientGetMsg(Message msg) {
        if (msg == null) Log.error("msg null!");
        if (clientGameModel == null) Log.error("clientGameModel is null!");
        clientGameModel.processMessage(msg);
    }

    public void lobbyClientReady(Team team, Role role) {
        client.notifyReady(team, role);
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
        System.out.println("want it to be null: " + gameView);
        return status;
    }
}

