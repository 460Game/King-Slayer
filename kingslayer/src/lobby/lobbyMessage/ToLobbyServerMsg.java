package lobby.lobbyMessage;

import lobby.Lobby;
import lobby.lobbyMessage.LobbyMessage;
import network.LobbyServer;

public abstract class ToLobbyServerMsg extends LobbyMessage {

    @Override
    public void execuate(Lobby lobby) {
        this.lobbyServerExecuate((LobbyServer) lobby);
    }

    public abstract void lobbyServerExecuate(LobbyServer lobbyServer);
}
