package game.network;

import java.io.IOException;


import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.Game.Model.GameModel;
import game.model.Game.Model.ServerGameModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.util.*;

public class LobbyServer extends Application {

    private ServerGameModel serverModel;
    private RemoteConnection server;
    private Set<RemoteConnection.RemoteModel> remoteModels;

    @Override
    public void start(Stage window) throws Exception {
        server = new RemoteConnection(true, this, new NetWork2LobbyAdaptor() {
            @Override
            public void init() {//should send the map
                serverModel.init(remoteModels);
                serverModel.start();

                Log.info(remoteModels.size() + " !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
                    Log.info("server start client: " + remoteModel.connectId + "!!!!!!!!!!");
                    remoteModel.startModel();
                }
            }

            @Override
            public void makeModel() {

            }

            @Override
            public void getMsg(Message obj) {
                serverGetMsg(obj);
            }
        });

        window.setTitle("Chat Server");

        //frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        /*frame.addWindowListener(new WindowAdapter() {
            public void windowClosed (WindowEvent evt) {
                server.stop();
            }
        });*/
      //  window.set(320, 200);

        Button startB = new Button("start button");
        startB.setOnAction(a -> {
            startGame();
        });


        Scene scene = new Scene(startB, 200, 100);
        window.setScene(scene);
        window.show();
    }

    public static void main(String[] args) throws IOException {
        Log.set(Log.LEVEL_DEBUG);
        Application.launch();
    }

    //TODO implement this
    public void serverGetMsg(Message msg) {
        serverModel.processMessage(msg);
    }

    public void startGame() {

        remoteModels = server.makeRemoteModel();
        serverModel = new ServerGameModel();

        for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
            remoteModel.syncClock();
        }
        Log.info("finish syn!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
            remoteModel.startGame();
        }


    }
    public void makeServerModel() {}

}

