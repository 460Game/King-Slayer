package game.model.game.model.worldObject.entity;

import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.aiStrat.AIStrat;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.deathStrat.DeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DrawData;
import game.model.game.model.worldObject.entity.drawStrat.DrawStrat;
import game.model.game.model.worldObject.entity.entities.Velocity;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import util.Util;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.*;
import static game.model.game.model.worldObject.entity.Entity.PropType.*;
import static util.Pair.pair;
import static util.Util.toDrawCoords;

/**
 * Represents any entity in the game world. Each entity knows the cells it is in
 * and knows its game-relevant data. Each entity has its own way of updating, colliding,
 * and drawing.
 */
public class Entity {

    private enum PropType{
        ON_CHANGE_ONLY, //sent only if changed on server
        ACTIVE_SYNC, //will queue an update to the client if changed
        PASSIVE_SYNC, //Will be sent along with any active sync items
        LOCAL_ONLY; // Not sent ever
    }

    /**
     * properties of the entity
     * all should be immutable unless local only - chanign data withen them without calls to set or put wont propgate
     */
    public enum EntityProperty {
        LAST_UPDATE_TIME(Integer.class, PASSIVE_SYNC),
        LEVEL(Integer.class, ACTIVE_SYNC),
        HEALTH(Double.class, ACTIVE_SYNC),
        HITBOX(Hitbox.class, ACTIVE_SYNC),
        X(Double.class, PASSIVE_SYNC),
        Y(Double.class, PASSIVE_SYNC),
        TEAM(Team.class, ACTIVE_SYNC),
        ROLE(Role.class, ACTIVE_SYNC),
        DRAW_DATA(DrawData.class, LOCAL_ONLY),
        VELOCITY(Velocity.class, ACTIVE_SYNC),
        AI_STRAT(AIStrat.class, LOCAL_ONLY),
        DRAW_STRAT(DrawStrat.class, ON_CHANGE_ONLY),
        UPDATE_STRAT(UpdateStrat.class, ON_CHANGE_ONLY),
        COLLISION_STRAT(CollisionStrat.class, ON_CHANGE_ONLY),
        DEATH_STRAT(DeathStrat.class, ON_CHANGE_ONLY);

        EntityProperty(Class type, PropType sync) {
            this.type = type;
            this.sync = sync;
        }

        public final Class type;
        public final PropType sync;
    }

    private Map<EntityProperty, Object> dataMap = new EnumMap<>(EntityProperty.class);

    public <T> Optional<T> oget(EntityProperty key) {
        return Optional.of((T) dataMap.get(key));
    }

    public <T> T get(EntityProperty key){
        return (T) dataMap.get(key);
    }

    public boolean has(EntityProperty key){
        return dataMap.containsKey(key);
    }

    public <T> void add(EntityProperty a, T value) {
        if(has(a))
            throw new RuntimeException("Already has property");
        dataMap.put(a, a.type.cast(value));
        if(a.sync ){//&& isServer) { TODO
            //Mark this to propogate to client
        }
    }

    private void add(Pair<EntityProperty, Object> pair) {
        this.add(pair.getKey(), pair.getValue());
    }

    //TODO better error messages
    public <T> void set(EntityProperty a, T value) {
        if(!has(a))
            throw new RuntimeException("Dosnt have prop");
        dataMap.put(a, a.type.cast(value));
        if(a.sync){ //&& isServer) { TODO
            //Mark this to propogate to client
        }
    }


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

    public transient long timeDelta = 0;

    /**
     * ID of this entity.
     */
    public final long id;


    public Entity(double x, double y, Pair<EntityProperty, Object>... varargs) {
        this.x = x;
        this.y =y;
        id = Util.random.nextLong();
        for(Pair<EntityProperty, Object> pair : varargs)
            add(pair);
    }

    /**
     * Constructor of an entity, given all of its game data.
     *
     * @param x              x-coordinate of the center of its position
     * @param y              y-coordinate of the center of its position
     * @param team           team corresponding to this entity
     * @param updateStrat    method with which this entity updates
     * @param collisionStrat method with which this entity collides.
     * @param hitbox         getHitbox of this entity
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
        this(x,y,pair(TEAM, team), pair(ROLE, role), pair(UPDATE_STRAT, updateStrat), pair(COLLISION_STRAT, collisionStrat), pair(DRAW_STRAT, drawStrat),
                pair(AI_STRAT, aiStrat), pair(HITBOX, hitbox), pair(DEATH_STRAT, deathStrat),
                pair(HEALTH, health));
        updateStrat.initUpdateData(this);
        aiStrat.makeAIData(this);
    }

    /**
     * Default constructor needed for serialization.
     */
    private Entity() {
        id = 0;
    }

    public void updateAI(ServerGameModel model, double secondsElapsed) {
        if (this.getHealth() <= 0) {
            entityDie(model);
        } else {
            this.<AIStrat>oget(AI_STRAT).ifPresent(strat -> strat.updateAI(this, model, secondsElapsed));
        }
    }

    public void draw(GraphicsContext gc) {
        this.<DrawStrat>oget(DRAW_STRAT).ifPresent(strat -> strat.draw(this, gc));

        if (!this.invincible()) {
            //TEMPORARY!!!!!!!!!
            gc.setFill(Color.RED);
            gc.fillRect(toDrawCoords(getX()) - 10, toDrawCoords(getX()) - 30, 20, 3);
            gc.setFill(Color.GREEN);
            gc.fillRect(toDrawCoords(getX()) - 10, toDrawCoords(getX()) - 30, (getHealth() / 100) * 20, 3);
        }
    }

    public Hitbox getHitbox() {
        return this.<Hitbox>get(HITBOX);
    }

    public Double getX() {
        return this.<Double>get(X);
    }

    public Double getY() {
        return this.<Double>get(Y);
    }
    public void setX(double x) {
        this.set(X, x);
    }

    public Double setY(double y) {
        this.set(X, y);
    }


    private boolean invincible() {
        return getHealth() == Double.POSITIVE_INFINITY;
    }

    public double getDrawZ() {
        return this.<DrawStrat>get(DRAW_STRAT).getDrawZ(this);
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
        this.<CollisionStrat>oget(COLLISION_STRAT).ifPresent(collisionStrat -> collisionStrat.collision(model, this, b));
    }

    public void update(GameModel model) {
        inCollision = false;
        if (this.lastUpdateTime == -1) {
            this.lastUpdateTime = model.modelCurrentTime;
            return;
        }

        this.timeDelta = model.modelCurrentTime - this.data.lastUpdateTime;
        this.data.lastUpdateTime = model.modelCurrentTime;

            this.updateStrat.update(this, model);

            drawStrat.update(this, model);

    }

    /**
     * Gets the collision type of this entity.
     *
     * @return the collision type of this entity
     */
    public CollisionStrat.CollideType getCollideType() {
        return this.<CollisionStrat>get(COLLISION_STRAT).getCollideType();
    }

    /**
     * Update the cells that this entity is currently in.
     *
     * @param model current model of the game
     */
    public void updateCells(GameModel model) {
        this.getHitbox().updateCells(this, model);
    }

    public String toString() {
        return "" + this.id + ": " + this.getX() + ", " + this.getY();
    }

    public boolean checkCollision(Hitbox hitbox, double x, double y) {
        return Hitbox.testCollision(this.getX(), this.getY(), this.getHitbox(), x, y, hitbox);
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
                clientGameModel -> {
                }
        );
        this.<DeathStrat>oget(DEATH_STRAT).ifPresent(strat -> strat.handleDeath(model, this));
    }

    public void decreaseHealthBy(GameModel model, double decrement) {
        this.set(HEALTH, this.<Double>get(HEALTH) - decrement);
        if (this.<Double>get(HEALTH) <= 0)
            entityDie(model);
    }

    public double getHealth() {
        return this.<Double>get(HEALTH);
    }

    public void onDeath(ServerCallBack deathHandler) {
        serverDeathCallBacks.add(deathHandler);
    }
}
