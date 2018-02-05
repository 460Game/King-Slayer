package game.model.game.model.worldObject.entity.collideStrat;

import game.model.game.grid.GridCell;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.collideData.CellCollisionData;
import game.model.game.model.worldObject.entity.collideStrat.collideData.CollisionData;
import javafx.scene.canvas.GraphicsContext;

import java.util.Set;

public class HardCollisionStrat extends CollisionStrat{

    private static HardCollisionStrat SINGLTON = new HardCollisionStrat();

    @Override
    public CollisionData initCollisionData() {
        return new CellCollisionData();
    }

    public final void collision(GameModel model, Entity b) {
        if(b.getCollideType() == CollideType.HARD)
            throw new RuntimeException("HARD objects overlapping");
        //else no-op
    }

    @Override
    public final CollideType getCollideType() {
        return CollideType.HARD;
    }

    public static HardCollisionStrat make() {
        return SINGLTON;
    }
}
