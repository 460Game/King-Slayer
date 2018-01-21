package game.model.Game.Map;

import Util.Loc;
import game.model.Game.WorldObject.Entity.Entity;

import java.util.Collection;
import java.util.List;

public interface MapGenerator {

    Tile makeTile(int i, int j);
    Collection<Entity> makeStartingEntities();
    public List<Loc> getStartingLocations() ;
}
