package game.model.Game;

import Util.Loc;
import game.model.Game.Tile.Tile;

import java.util.List;
import java.util.Random;

public interface MapGenerator {

    Tile makeTile(int i, int j);
    public List<Loc> getStartingLocations() ;
}
