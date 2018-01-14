package game.model.Game.WorldObject;

import game.model.Game.GameModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TestPlayer extends Entity {

    double dx = 0;
    double dy = 0;
    double x = 0;
    double y = 0;

    public TestPlayer(GameModel model, int x, int y) {
        super(model);
        this.x = x;
        this.y = y;
    }

    @Override
    public void collision(Entity b) {
        dx = 0;
        dy = 0;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x*10,y*10, 10, 10);
    }

    @Override
    public void update(long time, GameModel model) {
        x += dx * time * 1e-9 * 10;
        y += dy * time * 1e-9 * 10;
        System.out.println("Player location: " + x + ", " + y);
    }

    public void up() {
        dy = -1;
    }

    public void left() {
        dx = -1;
    }

    public void right() {
        dx = 1;
    }

    public void down() {
        dy = 1;
    }

    public void stopVert() {
        dy = 0;
    }

    public void stopHorz() {
        dx = 0;
    }
}
