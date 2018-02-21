package lobby.lobbyMessage;

import lobby.Lobby;
import lobby.lobbyMessage.LobbyMessage;
import network.LobbyClient;

public abstract class ToLobbyClientMsg extends LobbyMessage {

    @Override
    public void execuate(Lobby lobby) {
        this.lobbyClientExecuate((LobbyClient) lobby);
    }

    public abstract void lobbyClientExecuate (LobbyClient lobbyClient);
}
