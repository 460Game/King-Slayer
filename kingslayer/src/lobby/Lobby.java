package lobby;

import lobby.lobbyMessage.LobbyMessage;

public interface Lobby {
    public void processMessage(LobbyMessage msg);
}
