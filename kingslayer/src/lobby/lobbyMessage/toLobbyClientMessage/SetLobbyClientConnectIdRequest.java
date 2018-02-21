package lobby.lobbyMessage.toLobbyClientMessage;

import lobby.lobbyMessage.ToLobbyClientMsg;
import network.LobbyClient;

public class SetLobbyClientConnectIdRequest extends ToLobbyClientMsg {
    public SetLobbyClientConnectIdRequest() {}
    public SetLobbyClientConnectIdRequest(int connectId) {}
    @Override
    public void lobbyClientExecuate(LobbyClient lobbyClient) {
//        lobbyClient.connectId =
        //TODO: implement this first
    }
}
