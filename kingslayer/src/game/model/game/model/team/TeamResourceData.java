package game.model.game.model.team;

/**
 * This class holds the data for the whole team. Most of this
 * data is simply the resource counter for a team. This should
 * always be set by the server - the client can't change it!
 */
public class TeamResourceData {

    /**
     * Amount of wood resources on the team.
     */
    private int woodAmount;

    /**
     * Amount of stone resources on the team.
     */
    private int stoneAmount;

    /**
     * Amount of metal resources on the team.
     */
    private int metalAmount;

    //Get the IDs of a player
    // so the first king would be playerIds[King.val][0]
    // the first slayer would be playerIds[Slayer.val][Val]
    //private long[][] playerIds[];

    /**
     * Constructor for a team's resources. Initially, a team
     * has 0 of all resources.
     */
    public TeamResourceData() {
        woodAmount = 0;
        stoneAmount = 0;
        metalAmount = 0;
    }

    /**
     * Gets the amount of wood on the team.
     * @return the amount of wood on the team
     */
    public int getWood() {
        return woodAmount;
    }

    /**
     * Gets the amount of stone on the team.
     * @return the amount of stone on the team
     */
    public int getStone() {
        return stoneAmount;
    }

    /**
     * Gets the amount of metal on the team.
     * @return the amount of metal on the team
     */
    public int getMetal() {
        return metalAmount;
    }

    /**
     * TODO
     * @param i
     */
    public void increaseWood(int i) {
        woodAmount++;
    }
}
