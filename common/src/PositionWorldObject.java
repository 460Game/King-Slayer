import java.util.Set;

public abstract class PositionWorldObject<T extends PositionWorldObject.PositionWorldObjectData> extends WorldObject<T> {

    public static class PositionWorldObjectData<T extends PositionWorldObject> extends WorldObjectData<T> {
        double x;
        double y;
    }

    public abstract boolean isRound();
    public abstract boolean isRect();

    PositionWorldObject(double x, double y, GameModel model) {
        this.tiles = getTiles(model.map);
    }

    @Override
    public void set(T t) {
        data.x = t.x;
        data.y = t.y;
    }

    @Override
    public void update(GameModel model) {
        double old_x = data.x;
        double old_y = data.y;
        super.update(model);
        if(old_x != data.x || old_y != data.y) {

            //TODO we want to transparently keep this tile data structure up to date without too much expense
            Set<Tile> newTiles = getTiles(model.map);

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
    abstract void collision(PositionWorldObject<?> other);

    abstract Set<Tile> getTiles(Map map);

    //set of tiles it is on
    private Set<Tile> tiles;

    static double dist(PositionWorldObject<?> a, PositionWorldObject<?> b) {
        return Math.sqrt( (a.data.x - b.data.x) + (a.data.y - b.data.y));
    }

    /**
     * should return true exactly if the two objects are colldiding
     * type dependent- ew!
     */
    static boolean testCollision(PositionWorldObject<?> a, PositionWorldObject<?> b)  {
        if(a.isRect() && b.isRect()) {
//TODO
        } else if(a.isRound() && b.isRound()) {
            return dist(a,b) <=  ((CircleWorldObject.CircleWorldObjectData)a.data).r + ((CircleWorldObject.CircleWorldObjectData)b.data).r;
        } else if(a.isRect() && b.isRound()) {
//TODO
        } else if(a.isRound() && b.isRect()) {
//TODO
        }
        throw new RuntimeException("all Position WorldObjects must either be circle or rectangle objects!");
    }
}
