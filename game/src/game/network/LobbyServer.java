package game.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    public LobbyServer() throws IOException {
        server = new RemoteConnection(true, this);

        JFrame frame = new JFrame("Chat Server");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosed (WindowEvent evt) {
                server.stop();
            }
        });
        frame.getContentPane().add(new JLabel("Close to stop the chat server."));
        frame.setSize(320, 200);
        frame.setLocationRelativeTo(null);
        JButton startB = new JButton("start button");
        startB.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent evt) {
                startGame();
            }
        });
        frame.add(startB);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        LobbyServer lobbyServer = new LobbyServer();
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
    }

}

