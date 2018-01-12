import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class ChatServer {
    Server server;
    public ChatServer () throws IOException {
        server = new Server() {
            protected Connection newConnection () {
                return new ChatConnection();
            }
        };
    }

    public void start() throws IOException {
        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and server.
        ChatCommon.register(server);

        server.addListener(new Listener() {
            public void received (Connection c, Object obj) {
                Log.info("received");
                // We know all connections for this server are actually ChatConnections.
                ChatConnection connection = (ChatConnection)c;



                if (obj instanceof ChatCommon.UserName) {
                    //for some reason It keeps receiving and I want to return here
                    if (connection.name != null) return;
                    // Ignore the object if the name is invalid.
                    String s_name = ((ChatCommon.UserName)obj).user_name;
                    if (s_name == null) return;
                    s_name = s_name.trim();
                    if (s_name.length() == 0) return;

                    // Ignore the object if the name is invalid.
                    String name = ((ChatCommon.UserName)obj).user_name;

                    connection.name = name;
                    // Send a "connected" message to everyone except the new client.
                    ChatCommon.ChatMsg chatMessage = new ChatCommon.ChatMsg(name + ": you are connected.");

                    System.out.println(chatMessage.msg);
//                    server.sendToAllExceptTCP(connection.getID(), chatMessage);
                    server.sendToAllTCP(chatMessage);

                    // Send everyone a new list of connection names.
                    updateNames(); //?
                    return;
                }
                if (obj instanceof ChatCommon.ChatMsg) {
                    ChatCommon.ChatMsg chatMsg = (ChatCommon.ChatMsg) obj;
                    if (chatMsg.msg == null) return;
                    ChatCommon.ChatMsg msgWithName = new ChatCommon.ChatMsg(connection.name + ": " + chatMsg.msg);
                    Log.info(msgWithName.msg);
                    // Prepend the connection's name and send to everyone.
                    server.sendToAllTCP(msgWithName);
                    return;
                }
            }

            public void disconnected (Connection c) {
                ChatConnection connection = (ChatConnection)c;
                if (connection.name != null) {
                    // Announce to everyone that someone (with a registered name) has left.
                    ChatCommon.ChatMsg chatMsg = new ChatCommon.ChatMsg(connection.name + " you are disconnected.");
                    server.sendToAllTCP(chatMsg);
                    updateNames(); //?
                }
            }
        });

        server.bind(ChatCommon.port);
        server.start();
//        Log.info(server.);

        // Open a window to provide an easy way to stop the server.
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
        frame.setVisible(true);
    }

    void updateNames () {
        // Collect the names for each connection.
        Connection[] connections = server.getConnections();
        ArrayList names = new ArrayList(connections.length);
        for (int i = connections.length - 1; i >= 0; i--) {
            ChatConnection connection = (ChatConnection)connections[i];
            names.add(connection.name);
        }
        // Send the names to everyone.
        ChatCommon.AllUserNames updateNames =
                new ChatCommon.AllUserNames((String[])names.toArray(new String[names.size()]));
        server.sendToAllTCP(updateNames);
    }

    // This holds per connection state.
    public static class ChatConnection extends Connection {
        public String name;
        public ChatConnection() {}
    }

    public static void main (String[] args) throws IOException {
        ChatServer server = new ChatServer();
        server.start();
    }
}
