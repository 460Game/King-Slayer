package network;

import java.io.IOException;


import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.game.model.ServerGameModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import network.RemoteConnection;

import java.util.*;

public class LobbyServer { //extends Application {

    private ServerGameModel serverModel;
    private RemoteConnection server;
    private Set<RemoteConnection.RemoteModel> remoteModels;

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


    public void start() throws Exception {
        server = new RemoteConnection(true, this, new NetWork2LobbyAdaptor() {
            @Override
            public void init() {//should send the map
                if (serverModel == null) {
                    System.out.println("ServerModel is null");
                    System.exit(-1);
                }

                serverModel.init(remoteModels);
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
            public void makeModel() {

            }

            @Override
            public void getMsg(Message obj) {
                serverGetMsg(obj);
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

    public void startGame() {

        remoteModels = server.makeRemoteModel();
        serverModel = new ServerGameModel();

        for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
            remoteModel.syncClock();
        }

        Log.info("finish syn!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
            remoteModel.clientMakeModel();
        }


    }
    public void makeServerModel() {}

}

