package game.model.game.model.worldObject.entity.drawStrat;


import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import javafx.scene.canvas.GraphicsContext;

public abstract class ShapeDrawStrat extends DrawStrat {

    public static final DrawStrat HITBOX_DRAW_STRAT = new ShapeDrawStrat() {};

    public void draw(Entity entity, GraphicsContext gc) {
        entity.data.shape.draw(gc);
    }

    public double getDrawZ(EntityData entity) {
        return entity.shape.getY();
    }
}
