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
    private String name;
//    private ChatFrame chatFrame;
    private Stage window;
    private LobbyClient2LobbyAdaptor lobbyAdaptor;

    public LobbyClient(Stage window, LobbyClient2LobbyAdaptor lobbyAdaptor) {
        this.window = window;
        this.lobbyAdaptor = lobbyAdaptor;
    }


    public void start() throws Exception {

        client = new RemoteConnection(false, this, new NetWork2LobbyAdaptor() {
            @Override
            public void serverInit(Team team, Role role) {
                //should not use this
            }

            @Override
            public void clientInit() {
                Log.debug("client init");

                gameView = new GameView(clientGameModel);
                Platform.runLater(()-> {
                    gameView.start(window);
                });

                Log.debug("Client Started");
            }

            @Override
            public void makeModel() {
                System.out.println("Client make model call!!!!!!!!!!");
                lobbyClientMakeModel();
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
        //TODO: might have a bug here
        System.out.println("check null: " + serverModel);
//        serverModel.notifyReady();
        client.notifyReady(team, role);
    }

    //TODO rename this to makeModel
    public void lobbyClientMakeModel() {
        //TODO kinda unsafe here. server might not have a model yet
        Log.info("lobby client has made the server model");
        serverModel = client.makeRemoteModel().iterator().next();

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


//    static private class ChatFrame extends JFrame {
//        CardLayout cardLayout;
//        JProgressBar progressBar;
//        JList messageList;
//        JTextField sendText;
//        JButton sendButton;
//        JList nameList;
//
//        public ChatFrame(String host) {
//            super("Chat Client");
//            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            setSize(640, 200);
//            setLocationRelativeTo(null);
//
//            Container contentPane = getContentPane();
//            cardLayout = new CardLayout();
//            contentPane.setLayout(cardLayout);
//            {
//                JPanel panel = new JPanel(new BorderLayout());
//                contentPane.add(panel, "progress");
//                panel.add(new JLabel("Connecting to " + host + "..."));
//                {
//                    panel.add(progressBar = new JProgressBar(), BorderLayout.SOUTH);
//                    progressBar.setIndeterminate(true);
//                }
//            }
//            {
//                JPanel panel = new JPanel(new BorderLayout());
//                contentPane.add(panel, "chat");
//                {
//                    JPanel topPanel = new JPanel(new GridLayout(1, 2));
//                    panel.add(topPanel);
//                    {
//                        topPanel.add(new JScrollPane(messageList = new JList()));
//                        messageList.setModel(new DefaultListModel());
//                    }
//                    {
//                        topPanel.add(new JScrollPane(nameList = new JList()));
//                        nameList.setModel(new DefaultListModel());
//                    }
//                    DefaultListSelectionModel disableSelections = new DefaultListSelectionModel() {
//                        public void setSelectionInterval(int index0, int index1) {
//                        }
//                    };
//                    messageList.setSelectionModel(disableSelections);
//                    nameList.setSelectionModel(disableSelections);
//                }
//                {
//                    JPanel bottomPanel = new JPanel(new GridBagLayout());
//                    panel.add(bottomPanel, BorderLayout.SOUTH);
//                    bottomPanel.add(sendText = new JTextField(), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
//                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
//                    bottomPanel.add(sendButton = new JButton("Send"), new GridBagConstraints(1, 0, 1, 1, 0, 0,
//                        GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0), 0, 0));
//                }
//            }
//
//            sendText.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent e) {
//                    sendButton.doClick();
//                }
//            });
//        }
//
//        public void setSendListener(final Runnable listener) {
//            sendButton.addActionListener(new ActionListener() {
//                public void actionPerformed(ActionEvent evt) {
//                    if (getSendText().length() == 0) return;
//                    listener.run();
//                    sendText.setText("");
//                    sendText.requestFocus();
//                }
//            });
//        }
//
//        public void setCloseListener(final Runnable listener) {
//            addWindowListener(new WindowAdapter() {
//                public void windowClosed(WindowEvent evt) {
//                    listener.run();
//                }
//
//                public void windowActivated(WindowEvent evt) {
//                    sendText.requestFocus();
//                }
//            });
//        }
//
//        public String getSendText() {
//            return sendText.getText().trim();
//        }
//
//        public void setNames(final String[] names) {
//            // This listener is run on the client's update thread, which was started by client.start().
//            // We must be careful to only interact with Swing components on the Swing event thread.
//            EventQueue.invokeLater(new Runnable() {
//                public void run() {
//                    cardLayout.show(getContentPane(), "chat");
//                    DefaultListModel model = (DefaultListModel) nameList.getModel();
//                    model.removeAllElements();
//                    for (String name : names)
//                        model.addElement(name);
//                }
//            });
//        }
//
//        public void addMessage(final String message) {
//            EventQueue.invokeLater(new Runnable() {
//                public void run() {
//                    DefaultListModel model = (DefaultListModel) messageList.getModel();
//                    model.addElement(message);
//                    messageList.ensureIndexIsVisible(model.size() - 1);
//                }
//            });
//        }
//    }

//    public static void main(String[] args) throws InterruptedException, IOException {
//        Log.set(Log.LEVEL_DEBUG);
//        Application.launch();
//    }

}

