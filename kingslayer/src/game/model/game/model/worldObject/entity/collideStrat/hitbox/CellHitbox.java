package game.model.game.model.worldObject.entity.collideStrat.hitbox;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import util.Util;

import java.util.Collections;
import java.util.Set;

import static util.Util.toDrawCoords;

/**
 * A hitbox that always takes up an entire cell, for entities that
 * block an entire cell.
 */
public class CellHitbox extends Hitbox {

    //TODO no reason this cant just be a 1x1 box

    public static final Hitbox SINGLETON = new CellHitbox();

    private CellHitbox() {
    }

    @Override
    public Set<GridCell> getCells(Entity entity, GameModel model) {
        return Collections.singleton(model.getCell((int)Math.floor(entity.data.x), (int)Math.floor(entity.data.y)));
    }

    @Override
    public void drawShape(GraphicsContext gc, Entity entity) {
        gc.fillRect(toDrawCoords(entity.data.x - 0.5), toDrawCoords(entity.data.y - 0.5), toDrawCoords(1), toDrawCoords(1));
    }

    @Override
    public double getRadius(double angle) {
        return 0.5; //TODO
    }
}
