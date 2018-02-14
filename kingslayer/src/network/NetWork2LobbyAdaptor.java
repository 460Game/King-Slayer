package network;

import game.message.Message;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import lobby.RoleChoice;
import lobby.TeamChoice;

public interface NetWork2LobbyAdaptor {
    public void serverInit(Team team, Role role);
    public void clientInit();
    public void makeModel();
    public void getMsg(Message obj);

    public void showLobbyTeamChoice();
    public void serverLobbyComfirmTeamAndRole(Integer connId, Team team, Role role);
}
