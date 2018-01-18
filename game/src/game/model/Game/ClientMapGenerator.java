package game.model.Game;

import Util.Loc;
import game.model.Game.Tile.Tile;

import java.util.List;

public class ClientMapGenerator implements MapGenerator {
    @Override
    public Tile makeTile(int i, int j) {
        return Tile.FOG;
    }

    @Override
    public List<Loc> getStartingLocations() {
        throw new RuntimeException("client shouldnt be selecting starting locations");
    }
}
