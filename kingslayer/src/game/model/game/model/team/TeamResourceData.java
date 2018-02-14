package game.model.game.model.team;

import java.util.HashMap;
import java.util.Map;

/**
 * This class holds the data for the whole team. Most of this
 * data is simply the resource counter for a team. This should
 * always be set by the server - the client can't change it!
 */
public class TeamResourceData {

    public enum Resource {
        WOOD(0),
        STONE(1),
        METAL(2);

        public int id;

        Resource(int id) {
            this.id = id;
        }
    }

    private Map<Resource, Integer> resources = new HashMap<>();

//    /**
//     * Amount of wood resources on the team.
//     */
//    private int woodAmount;
//
//    /**
//     * Amount of stone resources on the team.
//     */
//    private int stoneAmount;
//
//    /**
//     * Amount of metal resources on the team.
//     */
//    private int metalAmount;

    //Get the IDs of a player
    // so the first king would be playerIds[King.val][0]
    // the first slayer would be playerIds[Slayer.val][Val]
    //private long[][] playerIds[];

    /**
     * Constructor for a team's resources. Initially, a team
     * has 0 of all resources.
     */
    public TeamResourceData() {
        resources.put(Resource.WOOD, 20);//0);
        resources.put(Resource.STONE, 0);
        resources.put(Resource.METAL, 0);

//        woodAmount = 0;
//        stoneAmount = 0;
//        metalAmount = 0;
    }

    public int getResource(Resource r) {
        return resources.get(r);
    }

    public void setResource(Resource r, int num) {
        resources.put(r, num);
    }
}
