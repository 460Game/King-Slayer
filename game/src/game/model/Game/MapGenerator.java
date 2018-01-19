package game.model.Game;

import Util.Loc;
import game.model.Game.Tile.Tile;
import game.model.Game.WorldObject.Entity.Entity;

import java.util.Collection;
import java.util.List;
import java.util.Random;

public interface MapGenerator {

    Tile makeTile(int i, int j);
    Collection<Entity> makeStartingEntities();
    public List<Loc> getStartingLocations() ;
}
