package game.model.Game.WorldObject.Entity;

import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.CellShape;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;

/**
 * Treasure that can be found in the game world. Once picked up,
 * it immediately goes into that team's resource pool.
 */
public class Treasure extends StationaryEntity {

    /**
     * Shape of the treasure.
     */
    private Shape shape = new CellShape(); // TODO FIX THis

    @Override
    public Shape getShape() {
        return shape;
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
