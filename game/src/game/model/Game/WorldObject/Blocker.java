package game.model.Game.WorldObject;

import game.model.Game.GameModel;
import game.model.Game.WorldObject.Entity;
import game.model.Game.WorldObject.Shape.CellShape;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Blocker extends Entity {

    public Blocker(GameModel model, int x, int y) {
        super(model);
        this.shape = new CellShape(x,y);
    }

    private CellShape shape;

    @Override
    public void collision(GameModel model, Entity collidesWith) {
        //Do nothing
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void draw(GraphicsContext gc) {
       // shape.draw(gc);
        //Draw nothing
       // gc.setFill(Color.DARKORANGE);
       // gc.fillOval(x*10 + 2, y*10 + 2, 5, 5);
    }

    @Override
    public void update(long time, GameModel model) {
        //Do nothing
    }
}
