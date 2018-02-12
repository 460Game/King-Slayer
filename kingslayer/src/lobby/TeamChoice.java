package lobby;

import javafx.scene.paint.Color;

public enum TeamChoice {
    RED_TEAM(Color.RED),
    BLUE_TEAM(Color.BLUE);

    public Color teamColor;
    TeamChoice(Color color) {
        teamColor = color;
    }
}
