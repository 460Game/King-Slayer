package game.network;

import Util.Util;
import com.esotericsoftware.minlog.Log;
import game.message.Message;
import game.model.ClientGameModel;
import game.model.Game.ClientMapGenerator;
import game.model.Game.MapGenerator;
import game.model.IModel;
import game.view.ClientView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LobbyClient extends Application {
    private RemoteConnection client;
    private ClientGameModel clientGameModel;
    private RemoteConnection.RemoteModel serverModel;
    private ClientView clientView;
    private String name;
    private ChatFrame chatFrame;
    private Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.window = primaryStage;
        // Request the host from the user.
        String input = (String) JOptionPane.showInputDialog(null, "Host:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE,
            null, null, "localhost");
        if (input == null || input.trim().length() == 0) System.exit(1);
        final String host = input.trim();
        Log.info(host);

        // Request the user's name.
        input = (String) JOptionPane.showInputDialog(null, "Name:", "Connect to chat server", JOptionPane.QUESTION_MESSAGE, null,
            null, "Test");
        if (input == null || input.trim().length() == 0) System.exit(1);
        name = input.trim();

        // All the ugly Swing stuff is hidden in ChatFrame so it doesn't clutter the KryoNet example code.
        chatFrame = new ChatFrame(host);
        // This listener is called when the send button is clicked.
        chatFrame.setSendListener(new Runnable() {
            public void run() {
                //do whatever we what when clicking send
            }
        });
        // This listener is called when the chat window is closed.
        chatFrame.setCloseListener(new Runnable() {
            public void run() {
                client.stop();
            }
        });
        chatFrame.setVisible(true);

        client = new RemoteConnection(false, this);

        // We'll do the connect on a new thread so the ChatFrame can show a progress bar.
        // Connecting to localhost is usually so fast you won't see the progress bar.
        client.connectToServer(5000, host);

    }

    public void getMsg(Message msg) {
        clientGameModel.processMessage(msg);
    }

    public void startGame() {
        serverModel = client.makeRemoteModel().iterator().next();

        //TODO !!!! don't have getGenerator
        clientGameModel = new ClientGameModel(new IModel() {
            @Override
            public void processMessage(Message m) {
                Message copy = Util.KYRO.copy(m); //to simulate going thoruhg network - make a copy
                serverModel.processMessage(copy);
            }

            @Override
            public long nanoTime() {
                return serverModel.nanoTime();
            }
        }, new ClientMapGenerator());

        clientView = new ClientView(clientGameModel);
        Platform.runLater(()-> {
            clientView.start(window);
        });
    }


    static private class ChatFrame extends JFrame {
        CardLayout cardLayout;
        JProgressBar progressBar;
        JList messageList;
        JTextField sendText;
        JButton sendButton;
        JList nameList;

        public ChatFrame(String host) {
            super("Chat Client");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(640, 200);
            setLocationRelativeTo(null);

            Container contentPane = getContentPane();
            cardLayout = new CardLayout();
            contentPane.setLayout(cardLayout);
            {
                JPanel panel = new JPanel(new BorderLayout());
                contentPane.add(panel, "progress");
                panel.add(new JLabel("Connecting to " + host + "..."));
                {
                    panel.add(progressBar = new JProgressBar(), BorderLayout.SOUTH);
                    progressBar.setIndeterminate(true);
                }
            }
            {
                JPanel panel = new JPanel(new BorderLayout());
                contentPane.add(panel, "chat");
                {
                    JPanel topPanel = new JPanel(new GridLayout(1, 2));
                    panel.add(topPanel);
                    {
                        topPanel.add(new JScrollPane(messageList = new JList()));
                        messageList.setModel(new DefaultListModel());
                    }
                    {
                        topPanel.add(new JScrollPane(nameList = new JList()));
                        nameList.setModel(new DefaultListModel());
                    }
                    DefaultListSelectionModel disableSelections = new DefaultListSelectionModel() {
                        public void setSelectionInterval(int index0, int index1) {
                        }
                    };
                    messageList.setSelectionModel(disableSelections);
                    nameList.setSelectionModel(disableSelections);
                }
                {
                    JPanel bottomPanel = new JPanel(new GridBagLayout());
                    panel.add(bottomPanel, BorderLayout.SOUTH);
                    bottomPanel.add(sendText = new JTextField(), new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER,
                        GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    bottomPanel.add(sendButton = new JButton("Send"), new GridBagConstraints(1, 0, 1, 1, 0, 0,
                        GridBagConstraints.CENTER, 0, new Insets(0, 0, 0, 0), 0, 0));
                }
            }

            sendText.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sendButton.doClick();
                }
            });
        }

        public void setSendListener(final Runnable listener) {
            sendButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (getSendText().length() == 0) return;
                    listener.run();
                    sendText.setText("");
                    sendText.requestFocus();
                }
            });
        }

        public void setCloseListener(final Runnable listener) {
            addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent evt) {
                    listener.run();
                }

                public void windowActivated(WindowEvent evt) {
                    sendText.requestFocus();
                }
            });
        }

        public String getSendText() {
            return sendText.getText().trim();
        }

        public void setNames(final String[] names) {
            // This listener is run on the client's update thread, which was started by client.start().
            // We must be careful to only interact with Swing components on the Swing event thread.
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    cardLayout.show(getContentPane(), "chat");
                    DefaultListModel model = (DefaultListModel) nameList.getModel();
                    model.removeAllElements();
                    for (String name : names)
                        model.addElement(name);
                }
            });
        }

        public void addMessage(final String message) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    DefaultListModel model = (DefaultListModel) messageList.getModel();
                    model.addElement(message);
                    messageList.ensureIndexIsVisible(model.size() - 1);
                }
            });
        }

        public static void main(String[] args) throws InterruptedException, IOException {
            Log.set(Log.LEVEL_DEBUG);
            Application.launch();
        }
    }
}
