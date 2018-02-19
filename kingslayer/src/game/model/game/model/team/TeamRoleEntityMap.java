package game.model.game.model.team;

/**
 * Class used to aid in assigning roles and teams to the clients
 * in the game. Holds a mapping of an entity's team and role to its
 * ID. Primarily used for player roles and teams, as neutral entities
 * share the same role and team.
 */
public class TeamRoleEntityMap {

    /**
     * Holds the mapping from team and role to entity ID.
     */
    private long[][] map;

    /**
     * Constructor for team and role entity mapping, given a number
     * of teams and roles.
     * @param teams number of teams
     * @param roles number of roles
     */
    public TeamRoleEntityMap(int teams, int roles) {
        map = new long[teams][roles];
    }

    /**
     * Default constructor needed for serialization.
     */
    public TeamRoleEntityMap() {
        map = new long[0][0];
    }

    /**
     * Sets the team and role to the given ID in the mapping.
     * @param team team of entity
     * @param role role of entity
     * @param id id of entity
     */
    public void setEntity(Team team, Role role, long id) {
        map[team.team][role.val] = id;
    }

    /**
     * Gets the entity with the given team and role.
     * @param team team of entity
     * @param role role of entity
     * @return the entity with the given team and role
     */
    public long getEntity(Team team, Role role) {
        return map[team.team][role.val];
    }
}
