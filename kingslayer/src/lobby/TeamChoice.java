package lobby;

import javafx.scene.paint.Color;

public enum TeamChoice {
    TEAM_ONE(1),
    TEAM_TWO(2);

//    public Color teamColor;
    public int teamNum;
    TeamChoice(int num) {
        teamNum = num;
    }
}
