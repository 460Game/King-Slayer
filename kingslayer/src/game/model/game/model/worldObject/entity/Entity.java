package game.model.game.model.worldObject.entity;

import com.esotericsoftware.minlog.Log;
import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.aiStrat.AIStrat;
import game.model.game.model.worldObject.entity.aiStrat.AIable;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.deathStrat.DeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.drawStrat.DrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;
import javafx.scene.paint.Color;
import util.Util;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static util.Util.toDrawCoords;

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

    /**
     * The way this entity dies in the game.
     */
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
     *
     * @param x              x-coordinate of the center of its position
     * @param y              y-coordinate of the center of its position
     * @param team           team corresponding to this entity
     * @param updateStrat    method with which this entity updates
     * @param collisionStrat method with which this entity collides.
     * @param hitbox         hitbox of this entity
     * @param drawStrat      method with which this entity is drawn
     * @param aiStrat        TODO
     */
    public Entity(double x, double y, double health,
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
            updateStrat.initUpdateData(), x, y, health);
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
    public void updateAI(ServerGameModel model, double secondsElapsed) {
        if (this.getHealth() <= 0) {
            entityDie(model);
        } else {
            this.aiStrat.updateAI(this, model, secondsElapsed);
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        this.drawStrat.draw(this, gc);

        if (!this.invincible()) {
            //TEMPORARY!!!!!!!!!
            gc.setFill(Color.RED);
            gc.fillRect(toDrawCoords(data.x) - 10, toDrawCoords(data.y) - 30, 20, 3);
            gc.setFill(Color.GREEN);
            gc.fillRect(toDrawCoords(data.x) - 10, toDrawCoords(data.y) - 30, (getHealth() / 100) * 20, 3);
        }
    }

    private boolean invincible() {
        return getHealth() == Double.POSITIVE_INFINITY;
    }

    @Override
    public double getDrawZ() {
        return this.drawStrat.getDrawZ(data);
    }

    /**
     * Performs collisions with another entity based off of this
     * entity's colliding strategy.
     *
     * @param model current model of the game
     * @param b     the entity being collided with
     */
    public void collision(GameModel model, Entity b) {
        inCollision = true;
        this.collisionStrat.collision(model, this, b);
    }

    @Override
    public void update(GameModel model) {
        inCollision = false;

        // TODO temp fix. entity is still in the model...
        if (this.getHealth() <= 0) {
            entityDie(model);
//            System.out.println("HEELLLLLLLLLLLLLLLOOOOOOOO");
        }
        else {
            this.updateStrat.update(this, model);

            drawStrat.update(this, model);
        }
    }

    /**
     * Gets the collision type of this entity.
     *
     * @return the collision type of this entity
     */
    public CollisionStrat.CollideType getCollideType() {
        return collisionStrat.getCollideType();
    }

    /**
     * Update the cells that this entity is currently in.
     *
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

    //Experinamenting with this style of coding
    //Lots of the uglyness we have with strategies is stuff that touches the network
    //Theres no reason we cant have more beautiful code on the server side
    private transient List<ServerCallBack> serverDeathCallBacks = new ArrayList<>(0);

    public void entityDie(GameModel model) {
        model.execute(
            serverGameModel ->
                serverDeathCallBacks.forEach(
                    serverCallBack -> serverCallBack.accept(this, serverGameModel)),
            clientGameModel -> {}
        );
        deathStrat.handleDeath(model, this);
    }

    public void decreaseHealthBy(GameModel model, double decrement) {
        data.setHealth(data.getHealth() - decrement);
        if (data.getHealth() <= 0) {
            entityDie(model);
            System.out.println("ENTITY DIES");
        }
    }

    public double getHealth() {
        return data.getHealth();
    }

    public void onDeath(ServerCallBack deathHandler) {
        serverDeathCallBacks.add(deathHandler);
    }
}
