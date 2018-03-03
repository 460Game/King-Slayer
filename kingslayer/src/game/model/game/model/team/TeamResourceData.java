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

    public static Map<Integer, Resource> levelToResource = new HashMap<>();
    static {
        levelToResource.put(0, Resource.WOOD);
        levelToResource.put(1, Resource.STONE);
        levelToResource.put(2, Resource.METAL);
    }

    private Map<Resource, Integer> resources = new HashMap<>();

    /**
     * Constructor for a team's resources. Initially, a team
     * has 0 of all resources.
     */
    public TeamResourceData() {
        resources.put(Resource.WOOD, 10);
        resources.put(Resource.STONE, 0);
        resources.put(Resource.METAL, 0);
    }

    public int getResource(Resource r) {
        return resources.get(r);
    }

    public void setResource(Resource r, int num) {
        resources.put(r, num);
    }
}
