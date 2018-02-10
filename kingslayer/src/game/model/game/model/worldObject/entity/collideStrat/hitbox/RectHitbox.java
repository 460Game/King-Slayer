package game.model.game.model.worldObject.entity.collideStrat.hitbox;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import util.Const;

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
            for (int i = Math.max(0, (int) (entity.data.x - w)); i <= Math.min(Math.ceil(entity.data.x + w), Const.GRID_X_SIZE - 1); i++)
                for (int j = Math.max(0, (int) (entity.data.y - h)); j <= Math.min(Math.ceil(entity.data.y + h), Const.GRID_Y_SIZE - 1); j++)
                    set.add(gameMap.getCell(i, j));
        return set;
    }

    @Override
    public void drawShape(GraphicsContext gc, Entity entity) {
        throw new RuntimeException("NOt implemented yet");
    }

    @Override
    public double getRadius(double angle) {
        return 0.499; //TODO NONONO
      //  throw new RuntimeException("NOt implemented yet");
    }
}
