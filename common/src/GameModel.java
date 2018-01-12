import javafx.scene.canvas.GraphicsContext;

/**
 * adds some game logic for looking up objects by location
 * specificily has the grid
 */
public abstract class GameModel extends Model {

    GameMap gameMap = getGameMap();

    public abstract GameMap getGameMap();

    public void update() {
        super.updateAll();
        gameMap.update();
    }

    @Override
    public void draw(GraphicsContext gc) {

        super.drawAll(gc);

        this.gameMap.draw(gc);
    }

    public void reqUpdate(WorldObject u) {
        u.update(this);
    }

}
