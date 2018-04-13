package network;

import game.message.Message;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import lobby.PlayerInfo;

import java.util.Map;

public interface NetWork2LobbyAdaptor {
    public void serverInit();
    public void clientInit();
    public void makeModel();
    public void getMsg(Message obj);

    public void showLobbyTeamChoice();
    public void showLobbyTeamChoice(int numOnTeam);
    public void serverLobbyTrySetTeamAndRole(Integer connId, Team team, Role role, String playerName, int slayerIdx);
    public void serverStartRematch();

    void clientTakeSelectFb(boolean s, Map<Integer, PlayerInfo> map);

    int getNumOnTeam();

    Map<Integer, PlayerInfo> getPlayerInfoMap();

    public void readyButtonFb(boolean status);

    public void roleReadLock(PlayerInfo info);
}
