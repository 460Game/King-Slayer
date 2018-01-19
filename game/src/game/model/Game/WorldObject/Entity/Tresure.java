package game.model.Game.WorldObject.Entity;

import game.model.Game.GameModel;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;

public class Tresure extends Entity {
    @Override
    public void collision(GameModel model, Entity collidesWith) {

    }

    @Override
    public Shape getShape() {
        return null;
    }

    @Override
    public void update(long time, GameModel model) {

    }

    @Override
    public void draw(GraphicsContext gc) {

    }

    @Override
    public double getDrawZ() {
        return 0;
    }
}
