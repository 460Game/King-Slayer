package network;

import lobby.PlayerInfo;

import java.util.Map;

public interface LobbyClient2LobbyAdaptor {
    public void showChoiceTeamAndRoleScene();

    void takeSelectFb(boolean s, Map<String, PlayerInfo> map);

    void setNumOnTeam(int numOnTeam);
}
