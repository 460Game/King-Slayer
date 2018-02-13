package network;

import game.message.Message;
import lobby.RoleChoice;
import lobby.TeamChoice;

public interface NetWork2LobbyAdaptor {
    public void serverInit(Enum<TeamChoice> team, Enum<RoleChoice> role);
    public void clientInit();
    public void makeModel();
    public void getMsg(Message obj);

    public void showLobbyTeamChoice();
    public void serverLobbyComfirmTeamAndRole(Integer connId, Enum<TeamChoice> team, Enum<RoleChoice> role);
}
