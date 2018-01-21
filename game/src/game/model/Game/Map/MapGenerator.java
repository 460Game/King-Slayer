package game.model.Game.Map;

import Util.Loc;
import game.model.Game.WorldObject.Entity.Entity;

import java.util.Collection;
import java.util.List;

/**
 * Interface for generating the game map.
 */
public interface MapGenerator {

    /**
     * Generates a tile.
     * @param i x-coordinate
     * @param j y-coordinate
     * @return a tile
     */
    Tile makeTile(int i, int j);

    /**
     * Creates the entities that start at the beginning of the game.
     * @return a collection of the starting entities
     */
    Collection<Entity> makeStartingEntities();

    /**
     * Gets the starting locations of the players.
     * @return the starting locations of the players
     */
    public Collection<Loc> getStartingLocations();
}
