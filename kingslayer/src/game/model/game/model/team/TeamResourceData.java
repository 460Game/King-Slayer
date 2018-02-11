package game.model.game.model.team;

/*
team data
should always be set by server - client cant change!
 */
public class TeamResourceData {
    private int woodAmount;
    private int stoneAmount;
    private int metalAmount;

    //Get the IDs of a player
    // so the first king would be playerIds[King.val][0]
    // the first slayer would be playerIds[Slayer.val][Val]
    //private long[][] playerIds[];

    public TeamResourceData() {
        woodAmount = 0;
        stoneAmount = 0;
        metalAmount = 0;
    }

    public int getMetal() {
        return metalAmount;
    }

    public int getStone() {
        return stoneAmount;
    }

    public int getWood() {
        return woodAmount;
    }

    public void increaseWood(int i) {
        woodAmount++;
    }
}
