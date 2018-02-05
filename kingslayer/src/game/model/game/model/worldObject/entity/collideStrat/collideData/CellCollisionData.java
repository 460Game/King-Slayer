package game.model.game.model.worldObject.entity.collideStrat.collideData;

import javafx.scene.canvas.GraphicsContext;
import util.Util;

import java.util.Collections;
import java.util.Set;

import static util.Util.toDrawCoords;

/**
 * A collisionData that always takes up an entire cell, for entities that
 * block an entire cell.
 */
public class CellCollisionData extends RectCollisionData {

    public CellCollisionData() {
        super(1,1);
    }
}
