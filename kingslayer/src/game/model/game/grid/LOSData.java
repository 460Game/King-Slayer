package game.model.game.grid;

import game.model.game.model.team.Team;

public class LOSData {
    static enum LOSStatus {
        EXPLORED,
        VISABLE,
        HIDDEN
    }

    int[] losRange = new int[Team.values().length];

    LOSData() {

    }
}
