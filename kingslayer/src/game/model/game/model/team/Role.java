package game.model.game.model.team;

public enum Role {
    KING(0), SLAYER(1);

    public int val;

    Role(int i) {
        val = i;
    }

    /**
     * Return the team corresponding to the specified number.
     *
     * @param i an integer
     * @return the team corresponding to the specified number
     */
    public static Role valueOf(int i) {
        if (i == 0)
            return Role.KING;
        if (i == 1)
            return Role.SLAYER;
        throw new RuntimeException("Invalid role");
    }

}
