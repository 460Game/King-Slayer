package game.model.game.model.worldObject.entity.drawStrat;


import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import javafx.scene.canvas.GraphicsContext;

public class ShapeDrawStrat extends DrawStrat {

    public static final DrawStrat SINGLETON = new ShapeDrawStrat();

    private ShapeDrawStrat(){}

    @Override
    public DrawData initDrawData() {
        return null;
    }

    public void draw(Entity entity, GraphicsContext gc) {
        entity.data.hitbox.draw(gc, entity);
    }

    public double getDrawZ(EntityData entityData) {
        return entityData.y;
    }
}
