package game.model.game.model.worldObject.entity.collideStrat.hitbox;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;

import java.util.Set;

/**
 * TODO the logic in here is a pain because of handling rotation
 * will leave uncompleted until we need it :)
 */
public class CompositeHitbox extends Hitbox {
    //todo change to be more then composition of just 2
    private Hitbox a;

    private double offX;
    private double offY; // hitbox a is -offX/2, -offY/2 offset from center, hitbox b is offX/2 offY/2 from center

    private Hitbox b;

    @Override
    public Set<GridCell> getCells(Entity entity, GameModel gameMap) {
        Set<GridCell> set = a.getCells(entity, gameMap);
        set.addAll(b.getCells(entity, gameMap));
        return set;
    }

    @Override
    public void draw(GraphicsContext gc, Entity entity) {
        a.draw(gc, entity);
        b.draw(gc, entity);
    }

    @Override
    public double getRadius(double angle) {
        throw new RuntimeException("compoisite shape not yet done");
       // return Math.max(a.getRadius(angle), b.getRadius(angle)); //Needs to consider offset between them
    }
}
