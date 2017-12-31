package common.model;

import java.util.Collection;

public abstract class Tile {
    int x,y;

    //is passible if it is passible tile without building on it
    public boolean isPassible();

    //either contians a building
    //or can contin entitities
    Building building = null;
    Collection<Entity> contains();
}
