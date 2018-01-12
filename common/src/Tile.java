
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Tile implements Drawable {

    private Set<CollidingWorldObject> contains = new HashSet<>();

    //x,y coordiante of top left of this part of the grid
    public int x, y;

    //is passible if it is passible tile
    public abstract boolean isPassable();

    public Set<CollidingWorldObject> getContains() {
        return Collections.synchronizedSet(contains);
    }

    public void update() {
        for(CollidingWorldObject a : contains) {
            for (CollidingWorldObject b : contains) {
                if (a != b && CollidingWorldObject.testCollision(a, b)) {
                    a.collision(b);
                    b.collision(a); //todo would get doubled called if collision isnt resolved first time it is found
                }
            }
        }
    }

    public void add(CollidingWorldObject o) {
        contains.add(o);
    }

    public void remove(CollidingWorldObject o) {
        contains.remove(o);
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        if(!isPassable()) {
            //create 1x1 invisable barrier object at position x+1/2, y+1/2
        }
    }


    @Override public boolean equals(Object other) {
        Tile o = (Tile) other; //TODO do this properly
        return this.x == o.x && this.y == o.y;
    }


}
