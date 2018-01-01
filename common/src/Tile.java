
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class Tile implements Drawable {

    private Set<PositionWorldObject> contains = new HashSet<>();

    //x,y coordiante of top left of this part of the grid
    private int x, y;

    //is passible if it is passible tile
    public abstract boolean isPassable();

    public void update() {
        for(PositionWorldObject a : contains) {
            for (PositionWorldObject b : contains) {
                if (a != b && PositionWorldObject.testCollision(a, b)) {
                    a.collision(b);
                    b.collision(a); //todo would get doubled called if collision isnt resolved first time it is found
                }
            }
        }
    }

    public void add(PositionWorldObject o) {
        contains.add(o);
    }

    public void remove(PositionWorldObject o) {
        contains.remove(o);
    }

    public Tile(int x, int y, Model model) {
        this.x = x;
        this.y = y;
        if(!isPassable()) {
            //create 1x1 invisable barrier object at position x+1/2, y+1/2
        }
    }

}
