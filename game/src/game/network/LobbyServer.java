package game.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


import Util.Util;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import game.message.CreatePlayerMessage;
import game.message.Message;
import game.message.SetTileMessage;
import game.model.Game.GameModel;
import game.model.IModel;
import game.model.ServerGameModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import javax.swing.*;
import java.util.*;

public class LobbyServer extends Application {

    private GameModel serverModel;
    private RemoteConnection server;
    private Set<RemoteConnection.RemoteModel> remoteModels;

    @Override
    public void start(Stage window) throws Exception {
        server = new RemoteConnection(true, this);

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
        Application.launch();
    }
    //TODO implement this
    public void getMsg(Message msg) {
        serverModel.processMessage(msg);
    }

    public void startGame() {
        remoteModels = server.makeRemoteModel();
        serverModel = new ServerGameModel(remoteModels);
        for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
            remoteModel.startGame();
        }
        for(IModel client : remoteModels) {
            for (int i = 0; i < serverModel.getMapWidth(); i++)
                for (int j = 0; j < serverModel.getMapWidth(); j++)
                    client.processMessage(new SetTileMessage(i, j, serverModel.getTile(i, j)));
        }


        Iterator<? extends IModel> iter = (Iterator<? extends IModel>) remoteModels.iterator();
        iter.next().processMessage(new CreatePlayerMessage(serverModel.playerA));
        iter.next().processMessage(new CreatePlayerMessage(serverModel.playerB));

        Thread t = new Thread(()-> {
            while(true) {
                serverModel.update();
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

}

