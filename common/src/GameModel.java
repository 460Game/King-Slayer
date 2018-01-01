/**
 * adds some game logic for looking up objects by location
 * specificily has the grid
 */
public abstract class GameModel extends Model {

    Map map;

    @Override
    public void update() {
        super.update();
        map.update();
    }
}
