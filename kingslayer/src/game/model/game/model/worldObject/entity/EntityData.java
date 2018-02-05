package game.model.game.model.worldObject.entity;

import game.model.game.model.worldObject.entity.entities.Velocity;
import game.model.game.model.worldObject.entity.aiStrat.AIData;
import game.model.game.model.worldObject.entity.drawStrat.DrawData;
import game.model.game.model.worldObject.entity.collideStrat.collideData.CollisionData;
import game.model.game.model.worldObject.entity.updateStrat.UpdateData;

public class EntityData {
    public CollisionData collisionData;
    public AIData aiData;
    public DrawData drawData;
    public UpdateData updateData;

    public EntityData(CollisionData collisionData, AIData aiData, DrawData drawData, UpdateData updateData, double x, double y) {
        this.collisionData = collisionData;
        this.aiData = aiData;
        this.drawData = drawData;
        this.updateData = updateData;
        this.x = x;
        this.y = y;
        this.health = 1;
        this.velocity = new Velocity();
    }

    public double x;
    public double y;

    /**
     * Current health of the entity.
     */
    double health;

    /**
     * Current velocity of the entity.
     */
    Velocity velocity;

}
