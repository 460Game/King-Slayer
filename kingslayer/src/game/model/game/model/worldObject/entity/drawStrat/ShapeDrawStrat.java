package game.model.game.model.worldObject.entity.drawStrat;


import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class ShapeDrawStrat extends DrawStrat {

    public static final DrawStrat SINGLETON = new ShapeDrawStrat();

    private ShapeDrawStrat(){}

    @Override
    public DrawData initDrawData() {
        return null;
    }

    @Override
    public void draw(Entity entity, ClientGameModel model, GraphicsContext gc) {
        gc.setFill(Color.color(1,1,1,1));
        entity.getHitbox().draw(gc, entity);
    }

    public double getDrawZ(Entity entity) {
        return entity.getY();
    }
}
