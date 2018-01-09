import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public abstract class ServerModel extends GameModel {
    private ServerSocket server_socket;
    private ArrayList<Socket> client_sockets;
    public ServerModel () {
        try {
            server_socket = new ServerSocket(8000);
        } catch (IOException e) {
            System.err.println("new server socket error");
        }
        client_sockets = new ArrayList<>();
    }
    public void start() {
        while (true) {
            try {
                Socket new_client = server_socket.accept();
                client_sockets.add(new_client);
            } catch (IOException e) {
                System.err.println("server socket accept error");
            }
        }
    }

}
