package game.model.game.model.worldObject.entity;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.entities.Velocity;
import game.model.game.model.worldObject.entity.aiStrat.AIData;
import game.model.game.model.worldObject.entity.drawStrat.DrawData;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.updateStrat.UpdateData;

/**
 * Class that holds all the game-relevant data for an entity.
 */
public class EntityData {

    /**
     * Hit box of an entity. This is essentially the shape that
     * represents the entity
     */
    public Hitbox hitbox;

    /**
     * The data needed for the AI of this entity.
     */
    public AIData aiData;

    /**
     * The data needed to update the entity.
     */
    public DrawData drawData;

    /**
     * The data needed to update the entity.
     */
    public UpdateData updateData;

    /**
     * X-coordinate of the center of the entity.
     */
    public double x;

    /**
     * Y-coordinate of the center of the entity.
     */
    public double y;

    /**
     * Current health of the entity.
     * Please don't change it back to public
     */
    private double health;

    /**
     * Current velocity of the entity.
     */
    Velocity velocity;

    public long lastUpdateTime = -1;

    /**
     * Constructor for the data of an entity, given all of the relevant information.
     * @param hitbox hitbox of the entity
     * @param aiData data needed for the AI of this entity
     * @param drawData data needed to update this entity in the game
     * @param updateData data needed to update this entity
     * @param x x-coordinate of center of the entity
     * @param y y-coordinate of center of the enitty
     */
    public EntityData(Hitbox hitbox, AIData aiData, DrawData drawData, UpdateData updateData, double x, double y, double health) {
        this.hitbox = hitbox;
        this.aiData = aiData;
        this.drawData = drawData;
        this.updateData = updateData;
        this.x = x;
        this.y = y;
        this.health = health;
        this.velocity = new Velocity();
    }

    /**
     * Default constructor needed for serialization.
     */
    private EntityData() {}

    protected double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

}
