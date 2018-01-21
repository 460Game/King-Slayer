package game.model.Game.Map;

import Util.Loc;
import game.model.Game.WorldObject.Entity.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Map generator for the client side of the game.
 */
public class ClientMapGenerator implements MapGenerator {

    @Override
    public Tile makeTile(int i, int j) {
        return Tile.FOG;
    }

    @Override
    public Collection<Entity> makeStartingEntities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public List<Loc> getStartingLocations() {
        throw new RuntimeException("Client shouldn't be selecting starting locations");
    }
}
