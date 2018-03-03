package lobby;

import network.LobbyClient;
import network.LobbyServer;

public class MainController {
    LobbyClient lobbyClient = null;
    LobbyServer lobbyServer = null;

    public static void main(String[] args) {
        Main mainView;
        new Thread() {
            @Override
            public void run() {
                javafx.application.Application.launch(Main.class);
            }
        }.start();
    }
}
