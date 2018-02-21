package lobby.lobbyMessage;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import lobby.Lobby;
import network.LobbyServer;

public class LobbySeverValidateTeamRoleRequest extends ToLobbyServerMsg {

    Team myTeam;
    Role myRole;
    String playerName;

    public LobbySeverValidateTeamRoleRequest(Team team, Role role, String playerName) {
        this.myTeam = team;
        this.myRole = role;
        this.playerName = playerName;
    }

    @Override
    public void lobbyServerExecuate(LobbyServer lobbyServer) {
//        TODO: implement this secondly
//        lobbyServer.validateTeamRolePlayer(myTeam, myRole, playerName);
    }
}
