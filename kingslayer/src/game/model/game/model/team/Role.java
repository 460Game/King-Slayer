package game.model.game.model.team;

/**
 * Enumeration of the different possible roles a player
 * can have in the game.
 */
public enum Role {

    /**
     * The king can build buildings and explore/collect resources.
     */
    KING(0),

    /**
     * The slayer can attack enemy units and player to fight for
     * the king.
     */
    SLAYER(1);

    /**
     * Holds the integer corresponding to the roles on a team.
     */
    public int val;

    /**
     * Constructor of a role, given a number.
     * @param i an integer
     */
    Role(int i) {
        val = i;
    }

    /**
     * Return the role corresponding to the specified number.
     * @param i an integer
     * @return the role corresponding to the specified number
     */
    public static Role valueOf(int i) {
        if (i == 0)
            return Role.KING;
        if (i == 1)
            return Role.SLAYER;
        throw new RuntimeException("Invalid role");
    }

}
