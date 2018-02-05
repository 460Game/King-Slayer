package game.model.game.model.worldObject.entity.collideStrat.hitbox;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;

import java.util.HashSet;
import java.util.Set;

public class RectHitbox extends Hitbox {

    //x,y are center of the rectangle
    //w,h are the width and height
    //radius is the rotation in radians
    private double w, h;

    RectHitbox(double w, double h) {
        this.w = w;
        this.h = h;
    }

    RectHitbox() {
        this(1,1);
    }

    @Override
    public Set<GridCell> getCells(Entity entity, GameModel gameMap) {
        Set<GridCell> set = new HashSet<>();
            for (int i = (int) (entity.data.x - w); i <= Math.ceil(entity.data.x + w); i++)
                for (int j = (int) (entity.data.y - h); j <= Math.ceil(entity.data.y + h); j++)
                    set.add(gameMap.getCell(i, j));
        return set;
    }

    @Override
    public void draw(GraphicsContext gc, Entity entity) {
        throw new RuntimeException("NOt implemented yet");
    }

    @Override
    public double getRadius(double angle) {
        throw new RuntimeException("NOt implemented yet");
    }
}
