package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.worldObject.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.aiStrat.AIStrat;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.HardCollisionStrat;
import game.model.game.model.worldObject.entity.drawStrat.DrawData;
import game.model.game.model.worldObject.entity.drawStrat.DrawStrat;
import game.model.game.model.worldObject.entity.drawStrat.ImageDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.StillStrat;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;
import javafx.scene.canvas.GraphicsContext;

public abstract class Blockers {

    private static final DrawStrat BLOCKER_DRAW_STRAT = new DrawStrat() {
        @Override
        public DrawData initDrawData() {
            return new DrawData();
        }

        @Override
        public void draw(Entity entity, GraphicsContext gc) {
            //Do nothing
        }

        @Override
        public double getDrawZ(EntityData entity) {
            return 0;
        }
    };

}
