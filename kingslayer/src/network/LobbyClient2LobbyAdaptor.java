package network;

import lobby.PlayerInfo;

import java.util.Map;
import java.util.Set;

public interface LobbyClient2LobbyAdaptor {
    public void showChoiceTeamAndRoleScene();

    void takeSelectFb(boolean s, Map<Integer, PlayerInfo> map, Set<Integer> readySet);

    void setNumOnTeam(int numOnTeam);

    void roleReadyLock(PlayerInfo info);
}
