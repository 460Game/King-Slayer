package lobby;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;

public class PlayerInfo {
    Team team;
    Role role;
    String playerName;
    public PlayerInfo() {}

    public PlayerInfo(Team team, Role role, String playerName) {
        this.team = team;
        this.role = role;
        this.playerName = playerName;
    }

    public Role getRole() {
        return role;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Team getTeam() {
        return team;
    }
}
