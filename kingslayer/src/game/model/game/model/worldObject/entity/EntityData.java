package game.model.game.model.worldObject.entity;

import game.model.game.model.worldObject.entity.Entities.Velocity;
import game.model.game.model.worldObject.entity.aiStrat.AIData;
import game.model.game.model.worldObject.entity.drawStrat.DrawData;
import game.model.game.model.worldObject.shape.Shape;

public class EntityData {
    public Shape shape;
    public AIData aiData;
    public DrawData drawData;

    /**
     * Current health of the entity.
     */
    double health;

    /**
     * Current velocity of the entity.
     */
    Velocity velocity;

}
