package game.model.game.model.team;

/**
 * Created by ryanhan on 2/12/18.
 */
public class TeamRoleEntityMap {

    private long[][] map;

    public TeamRoleEntityMap(int teams, int roles) {
        map = new long[teams][roles];
    }

    public TeamRoleEntityMap() {
        map = new long[0][0];
    }

    public void setEntity(Team team, Role role, long id) {
        map[team.team][role.val] = id;
    }

    public long getEntity(Team team, Role role) {
        return map[team.team][role.val];
    }
}
