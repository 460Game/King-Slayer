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
 * block an entire cell (boxes, walls, etc.).
 */
public class CellHitbox extends Hitbox {

    //TODO no reason this cant just be a 1x1 box

    public static final Hitbox SINGLETON = new CellHitbox();

    private CellHitbox() {

    }

    @Override
    public Set<GridCell> getCells(Entity entity, GameModel model) {
        return Collections.singleton(model.getCell((int) Math.floor(entity.data.x), (int) Math.floor(entity.data.y)));
    }

    @Override
    public void drawShape(GraphicsContext gc, Entity entity) {
        gc.fillRect(toDrawCoords(entity.data.x - 0.5), toDrawCoords(entity.data.y - 0.5), toDrawCoords(1), toDrawCoords(1));
    }

    @Override
    public double getRadius(double angle) {
        double piAngle = angle % Math.PI;
        return 0.5 / (piAngle < Math.PI / 4 ?  Math.cos(piAngle) :
                (piAngle > 3 * Math.PI / 4 ? -Math.cos(piAngle) : Math.sin(piAngle)));


        // TODO MAY need to dbl check

        // RECTANGLE: length is from left to right, width is from top to bottom
        // cos(theta) =  length / (2 * radius) for theta < pi / 4
        // cos(pi / 2 - theta) = sin(theta) = width / (2 * radius) for pi / 4 < theta < pi / 2
        // -sin(theta) = cos(theta - pi / 2) = width / (2 * radius) for pi / 2 < theta < 3 * pi / 4
        // -cos(theta) = cos(pi - theta) = length / (2 * radius) for 3 * pi / 4 < theta < pi

    }
}
