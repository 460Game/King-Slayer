package game.model.Game.WorldObject.Entity;

import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;

public class Treasure extends StationaryEntity {

    @Override
    public Shape getShape() {
        return null;
    }

    @Override
    public void update(long time, GameModel model) {

    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {

    }

    @Override
    public double getDrawZ() {
        return 0;
    }
}
