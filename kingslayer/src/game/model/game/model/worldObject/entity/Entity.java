package game.model.game.model.worldObject.entity;

import game.model.game.grid.GridCell;
import game.model.game.model.worldObject.entity.aiStrat.AIStrat;
import game.model.game.model.worldObject.entity.aiStrat.AIable;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.drawStrat.DrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;
import util.Util;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import javafx.scene.canvas.GraphicsContext;

import java.util.Set;

public class Entity implements Updatable, Drawable, AIable {

    final AIStrat aiStrat;
    final DrawStrat drawStrat;
    final UpdateStrat updateStrat;
    final CollisionStrat collisionStrat;

    public transient boolean inCollision = false;
    public transient double prevX = -1;
    public transient double prevY = -1;

    /*
     * used to track which cells this enttiy is in in the LOCAL modal.
     * Never sent across network!
     * May be null (empty set)
     */
    public transient Set<GridCell> containedIn = null;

    /**
     * Team of this entity.
     */
    public final Team team;

    /**
     * ID of this entity.
     */
    public final long id;

    public EntityData data;

    public void collision(GameModel model, Entity b) {
        inCollision = true;
        this.collisionStrat.collision(model, this, b);
    }

    public Entity(double x,
                  double y,
                  Team team,
                  UpdateStrat updateStrat,
                  CollisionStrat collisionStrat,
                  Hitbox hitbox,
                  DrawStrat drawStrat,
                  AIStrat aiStrat) {
        id = Util.random.nextLong();
        this.updateStrat = updateStrat;

        this.drawStrat = drawStrat;

        this.aiStrat = aiStrat;
        this.team = team;
        this.collisionStrat = collisionStrat;
        this.data = new EntityData(hitbox,
            aiStrat.makeAIData(),
            drawStrat.initDrawData(),
            updateStrat.initUpdateData(),
            x, y);
    }

    private Entity() {
        this.updateStrat = null;
        this.collisionStrat = null;
        this.drawStrat = null;
        this.aiStrat = null;
        team = null;
        id = 0;
    }

    @Override
    public void updateAI(GameModel model) {
        this.aiStrat.updateAI(this, model);
    }

    @Override
    public void draw(GraphicsContext gc) {
        this.drawStrat.draw(this, gc);
    }

    @Override
    public double getDrawZ() {
        return this.drawStrat.getDrawZ(data);
    }

    @Override
    public void update(GameModel model) {
        inCollision = false;
        this.updateStrat.update(this, model);
    }

    public CollisionStrat.CollideType getCollideType() {
        return collisionStrat.getCollideType();
    }

    public void updateCells(GameModel model) {
        this.data.hitbox.updateCells(this, model);
    }
}
