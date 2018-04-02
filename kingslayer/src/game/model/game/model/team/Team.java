package game.model.game.model.team;

import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import util.Const;

/**
 * Enumeration of all possible teams in the game.
 */
public enum Team {

    /**
     * Team 1's players, minions, and towers.
     */
    RED_TEAM(0, Color.RED),

    /**
     * Team 2's players, minions, and towers.
     */
    BLUE_TEAM(1, Color.BLUE);

    /**
     * Team number of the entity.
     */
    public int team;

    /**
     * Color to represent the team.
     */
    public Color color;

    /**
     * Constructor for a team.
     * @param teamNumber number to represent team
     * @param color color to represent team
     */
    Team(int teamNumber, Color color) {
        this.team = teamNumber;
        this.color = color;
    }

    /**
     * Return the team corresponding to the specified number.
     * @param i an integer
     * @return the team corresponding to the specified number
     */
    public static Team valueOf(int i) {
        if(i == 1)
            return Team.RED_TEAM;
        if(i == 2)
            return Team.BLUE_TEAM;
        throw new RuntimeException("Invalid team number");
    }

    public Background getPanelBG() {
        if(team == 1)
            return Const.PANEL_BG_RED;
        return Const.PANEL_BG_BLUE;
    }
}
