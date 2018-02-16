package game.model.game.model.worldObject.entity;

import game.model.game.grid.GridCell;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.aiStrat.AIStrat;
import game.model.game.model.worldObject.entity.aiStrat.AIable;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.deathStrat.DeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.drawStrat.DrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;
import util.Util;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import javafx.scene.canvas.GraphicsContext;

import java.util.Set;

/**
 * Represents any entity in the game world. Each entity knows the cells it is in
 * and knows its game-relevant data. Each entity has its own way of updating, colliding,
 * and drawing.
 */
public class Entity implements Updatable, Drawable, AIable {

    /**
     * TODO
     */
    final AIStrat aiStrat;

    /**
     * The way this entity is drawn on the game map.
     */
    final DrawStrat drawStrat;

    /**
     * The way this entity is updated in the game.
     */
    final UpdateStrat updateStrat;

    /**
     * The way this entity collides with other entities in the game.
     */
    final CollisionStrat collisionStrat;

    final DeathStrat deathStrat;

    /**
     * Checks if this entity is currently colliding with another entity.
     */
    public transient boolean inCollision = false;

    /**
     * Records the x-coordinate when this entity was last updated.
     */
    public transient double prevX = -1;

    /**
     * Records the y-coordinate when this entity was last updated.
     */
    public transient double prevY = -1;

    /**
     * Used to track which cells this entity is in in the LOCAL model.
     * Never sent across the network! May be null (empty set).
     */
    public transient Set<GridCell> containedIn = null;

    /**
     * Team of this entity.
     */
    public final Team team;

    /**
     *
     */
    public final Role role;

    /**
     * ID of this entity.
     */
    public final long id;

    /**
     * Holds all the game-relevant data about this entity.
     */
    public EntityData data;

    /**
     * Constructor of an entity, given all of its game data.
     * @param x x-coordinate of the center of its position
     * @param y y-coordinate of the center of its position
     * @param team team corresponding to this entity
     * @param updateStrat method with which this entity updates
     * @param collisionStrat method with which this entity collides.
     * @param hitbox hitbox of this entity
     * @param drawStrat method with which this entity is drawn
     * @param aiStrat TODO
     */
    public Entity(double x, double y,
                  Team team,
                  Role role,
                  UpdateStrat updateStrat,
                  CollisionStrat collisionStrat,
                  Hitbox hitbox,
                  DrawStrat drawStrat,
                  AIStrat aiStrat,
                  DeathStrat deathStrat) {
        id = Util.random.nextLong();
        this.team = team;
        this.role = role;
        this.updateStrat = updateStrat;
        this.collisionStrat = collisionStrat;
        this.drawStrat = drawStrat;
        this.aiStrat = aiStrat;
        this.data = new EntityData(hitbox, aiStrat.makeAIData(), drawStrat.initDrawData(),
                updateStrat.initUpdateData(), x, y);
        this.deathStrat = deathStrat;
    }

    /**
     * Default constructor needed for serialization.
     */
    private Entity() {
        this.updateStrat = null;
        this.collisionStrat = null;
        this.drawStrat = null;
        this.aiStrat = null;
        this.deathStrat = null;
        team = null;
        role = null;
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

    /**
     * Performs collisions with another entity based off of this
     * entity's colliding strategy.
     * @param model current model of the game
     * @param b the entity being collided with
     */
    public void collision(GameModel model, Entity b) {
        inCollision = true;
        this.collisionStrat.collision(model, this, b);
    }

    @Override
    public void update(GameModel model) {
        inCollision = false;
        this.updateStrat.update(this, model);

        if (this.role != Role.NEUTRAL) {
            ((DirectionAnimationDrawStrat) drawStrat).update(this);
        } else if (this.data.getHealth() <= 0) {
            model.removeByID(this.id);
        }
    }

    /**
     * Gets the collision type of this entity.
     * @return the collision type of this entity
     */
    public CollisionStrat.CollideType getCollideType() {
        return collisionStrat.getCollideType();
    }

    /**
     * Update the cells that this entity is currently in.
     * @param model current model of the game
     */
    public void updateCells(GameModel model) {
        this.data.hitbox.updateCells(this, model);
    }

    public String toString() {
        return "" + this.id + ": " + this.data.x + ", " + this.data.y;
    }

    public boolean checkCollision(Hitbox hitbox, double x, double y) {
        return Hitbox.testCollision(this.data.x, this.data.y, this.data.hitbox, x, y, hitbox);
    }

    public void entityDie(GameModel model) {
        deathStrat.handleDeath(model, this);
    }

    public void decreaseHealthBy(GameModel model, double decrement) {
        data.setHealth(data.getHealth() - decrement);
        if (Math.abs(data.getHealth()) < 0.01) {
            deathStrat.handleDeath(model, this);
        }
    }
}
