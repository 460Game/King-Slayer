package game.model.Game.WorldObject;

/**
 * Enumeration of all possible teams in the game.
 */
public enum Team {

    /**
     * Neutral team generally include the map entities.
     */
    NEUTRAL(0),

    /**
     * Team 1's players, minions, and towers.
     */
    ONE(1),

    /**
     * Team 2's players, minions, and towers.
     */
    TWO(2);

    /**
     * Team number of the entity.
     */
    public int team;

    /**
     * Constructor for a team
     * @param teamNumber
     */
    Team(int teamNumber) {
        this.team = teamNumber;
    }
}
