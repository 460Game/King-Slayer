import java.util.Set;

public abstract class PositionWorldObject<T extends PositionWorldObjectData> extends WorldObject<T> {

    public abstract boolean isRound();
    public abstract boolean isRect();

    PositionWorldObject(double x, double y, Model model) {
        this.x = x;
        this.y = y;
        this.tiles = getTiles(model);
    }

    double x;
    double y;

    @Override
    public void set(T t) {
        this.x = t.x;
        this.y = t.y;
    }

    @Override
    public void update(Model model) {
        double old_x = x;
        double old_y = y;
        super.update(model);
        if(old_x != x || old_y != y) {

            //TODO we want to transparently keep this tile datastructure up to date without too much expense
            Set<Tile> newTiles = getTiles(model);

            //get the diff


            for(Tile tile : newTiles) {
                if(!tiles.contains(tile)) {
                    tile.remove(this);
                }
            }
        }
    }

    /**
     * called whenever collides with another object
     * called for both directions
     * for now it is required that after this call they will not be colliding
     */
    abstract void collision(PositionWorldObject other);

    abstract Set<Tile> getTiles(Model model);

    //set of tiles it is on
    private Set<Tile> tiles;

    /**
     * should return true exactly if the two objects are colldiding
     * type dependent- ew!
     */
    static boolean testCollision(PositionWorldObject a, PositionWorldObject b)  {
        if(a.isRect() && b.isRect()) {

        } else if(a.isRound() && b.isRound()) {

        } else if(a.isRect() && b.isRound()) {

        } else if(a.isRound() && b.isRect()) {

        }
        throw new RuntimeException("all Position WorldObjects must either be circle or rectangle objects!");
    }
}
