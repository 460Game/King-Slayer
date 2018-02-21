package game.model.game.model.worldObject.entity.drawStrat;


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

    public void draw(Entity entity, GraphicsContext gc) {
        gc.setFill(Color.color(1,1,1,1));
        entity.data.hitbox.draw(gc, entity);
    }

    public double getDrawZ(EntityData entityData) {
        return entityData.y;
    }
}
