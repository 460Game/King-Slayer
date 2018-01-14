package game.model.Game.WorldObject;

import game.model.Game.GameModel;
import game.model.Game.WorldObject.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Blocker extends Entity {

    private int x,y;

    public Blocker(GameModel model, int x, int y) {
        super(model);
        this.x = x;
        this.y = y;
    }

    @Override
    public void collision(Entity b) {
        //Do nothing
    }

    @Override
    public void draw(GraphicsContext gc) {
        //Draw nothing
        gc.setFill(Color.DARKORANGE);
        gc.fillOval(x*10 + 2, y*10 + 2, 5, 5);
    }

    @Override
    public void update(long time, GameModel model) {
        //Do nothing
    }
}
