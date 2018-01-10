import javafx.scene.canvas.GraphicsContext;

/**
 * adds some game logic for looking up objects by location
 * specificily has the grid
 */
public abstract class GameModel extends Model {

    Map map = getGameMap();

    public abstract Map getGameMap();

    public void update() {
        super.updateAll();
        map.update();
    }

    @Override
    public void draw(GraphicsContext gc) {

        super.drawAll(gc);

        this.map.draw(gc);
    }

    public void reqUpdate(WorldObject u) {
        u.update(this);
    }

}
