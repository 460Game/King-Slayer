package game.network;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;


import Util.Util;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import game.message.Message;
import game.model.Game.GameModel;
import game.model.IModel;
import game.model.ServerGameModel;


import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class LobbyServer {

    private GameModel serverModel;
    RemoteConnection server;
    Set<RemoteConnection.RemoteModel> remoteModels;
    public LobbyServer() {
        server = new RemoteConnection(true, this);

//        JFrame frame = new JFrame("Chat Server");
//        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowClosed (WindowEvent evt) {
//                server.stop();
//            }
//        });
//        frame.getContentPane().add(new JLabel("Close to stop the chat server."));
//        frame.setSize(320, 200);
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
//        LobbyServer serverModel = new LobbyServer();
//        serverModel.start();
    }
    //TODO implement this
    public void getMsg(Message msg) {}

    public void startGame() {
        remoteModels = server.makeRemoteModel();
        serverModel = new ServerGameModel(Collections.singleton(new IModel() {

            //is server processing or client processing?
            @Override
            public void processMessage(Message m) {
                Message copy = Util.KYRO.copy(m);
                //TODO what is in here?
                for (RemoteConnection.RemoteModel remoteModel : remoteModels) {
                    remoteModel.processMessage(m);
                }
            }

            @Override
            public long nanoTime() {
                return 0;
            }
        }));
    }

}

