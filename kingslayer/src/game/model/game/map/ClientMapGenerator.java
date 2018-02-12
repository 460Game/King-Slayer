package game.model.game.map;

import game.model.game.model.worldObject.entity.Entity;
import util.Loc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * map generator for the client side of the game.
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
