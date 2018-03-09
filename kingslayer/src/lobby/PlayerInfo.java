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

    public static PlayerInfo copyOf(PlayerInfo p) {
        PlayerInfo ret = new PlayerInfo(p.getTeam(), p.getRole(), p.getPlayerName());
        return ret;
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

//    @Override
//    public boolean equals(Object obj) {
//        return ((PlayerInfo) obj).role == role && ((PlayerInfo) obj).team == team;
//    }
//
//    @Override
//    public int hashCode() {
//        return team.toString().hashCode()/11 + role.toString().hashCode()*11;
//    }

}
