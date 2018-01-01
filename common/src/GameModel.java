/**
 * adds some game logic for looking up objects by location
 * specificily has the grid
 */
public abstract class GameModel extends Model {

    Map map = new Map(this);

    public void update() {
        super.updateAll();
        map.update();
    }

    public void reqUpdate(WorldObject u) {
        u.update(this);
    }
}
