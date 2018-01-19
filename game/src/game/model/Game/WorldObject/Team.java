package game.model.Game.WorldObject;

import javafx.scene.paint.Color;

/**
 * Enumeration of all possible teams in the game.
 */
public enum Team {

    /**
     * Neutral team generally include the map entities.
     */
    NEUTRAL(0, Color.BLACK),

    /**
     * Team 1's players, minions, and towers.
     */
    ONE(1, Color.RED),

    /**
     * Team 2's players, minions, and towers.
     */
    TWO(2, Color.BLUE);

    /**
     * Team number of the entity.
     */
    public int team;

    /**
     * Color to represent the team.
     */
    public Color color;

    /**
     * Constructor for a team
     * @param teamNumber
     */
    Team(int teamNumber, Color color) {
        this.team = teamNumber;
        this.color = color;
    }
}
