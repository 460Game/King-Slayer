package game.model.game.model.worldObject.entity;

import com.esotericsoftware.minlog.Log;
import game.model.game.grid.GridCell;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.aiStrat.AIData;
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
import util.*;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import javafx.scene.canvas.GraphicsContext;

import java.util.*;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.*;
import static util.Pair.pair;
import static util.Util.toDrawCoords;

/**
 * Represents any entity in the game world. Each entity knows the cells it is in
 * and knows its game-relevant data. Each entity has its own way of updating, colliding,
 * and drawing.
 */
public class Entity {

    private enum PropType {
        ON_CHANGE_ONLY, //sent only if changed on server TODO not supported
        ACTIVE_SYNC, //will queue an update to the client if changed
        PASSIVE_SYNC, //Will be sent along with any active sync items
        LOCAL_ONLY; // Not sent ever
    }

    /**
     * properties of the entity
     * all should be immutable unless local only - chanign data withen them without calls to set or put wont propgate
     */
    public enum EntityProperty {
        LAST_UPDATE_TIME(Long.class, PropType.PASSIVE_SYNC),
        LEVEL(Integer.class, PropType.ACTIVE_SYNC),
        HEALTH(Double.class, PropType.ACTIVE_SYNC),
        HITBOX(Hitbox.class, PropType.ACTIVE_SYNC),
        X(Double.class, PropType.PASSIVE_SYNC),
        Y(Double.class, PropType.PASSIVE_SYNC),
        TEAM(Team.class, PropType.ACTIVE_SYNC),
        ROLE(Role.class, PropType.ACTIVE_SYNC),
        DRAW_DATA(DrawData.class, PropType.LOCAL_ONLY),
        VELOCITY(Velocity.class, PropType.ACTIVE_SYNC),
        AI_STRAT(AIStrat.class, PropType.LOCAL_ONLY),
        AI_DATA(AIData.class, PropType.LOCAL_ONLY),
        DRAW_STRAT(DrawStrat.class, PropType.ON_CHANGE_ONLY),
        UPDATE_STRAT(UpdateStrat.class, PropType.ON_CHANGE_ONLY),
        COLLISION_STRAT(CollisionStrat.class, PropType.ON_CHANGE_ONLY),
        DEATH_STRAT(DeathStrat.class, PropType.ON_CHANGE_ONLY);

        EntityProperty(Class type, PropType sync) {
            this.type = type;
            this.sync = sync;
        }

        public final Class type;
        public final PropType sync;
    }

    private EnumMap<Entity.EntityProperty, Object> localMap = new EnumMap<>(EntityProperty.class);
    private EnumMap<Entity.EntityProperty, Object> dataMap = new EnumMap<>(EntityProperty.class);


    public void setData(EnumMap<Entity.EntityProperty, Object> data) {
        this.dataMap = data;
    }

    public EnumMap<Entity.EntityProperty, Object> getData() {
        return this.dataMap;
    }


    public <T> Optional<T> oget(EntityProperty key) {
        if (has(key))
            return Optional.of((T) dataMap.get(key));
        return Optional.empty();
    }

    public <T> T getOrDefault(EntityProperty key, T def) {
        if(has(key))
            return this.<T>get(key);
        else return def;
    }

    public <T> T get(EntityProperty key) {
        if (key.sync == PropType.LOCAL_ONLY)
            return (T) localMap.get(key);
        return (T) dataMap.get(key);
    }

    public boolean has(EntityProperty key) {
        if (key.sync == PropType.LOCAL_ONLY)
            return localMap.containsKey(key);
        return dataMap.containsKey(key);
    }

    public <T> void add(EntityProperty key, T value) {
        if (has(key))
            throw new RuntimeException("Already has property");
        if (key.sync == PropType.LOCAL_ONLY)
            localMap.put(key, key.type.cast(value));
        else {
            dataMap.put(key, key.type.cast(value));

            if (key.sync == PropType.ACTIVE_SYNC)
                needSync = true;
        }
    }

    private void add(Pair<EntityProperty, Object> pair) {
        this.add(pair.getKey(), pair.getValue());
    }

    //TODO better error messages
    public <T> void set(EntityProperty key, T value) {
        if (!has(key))
            throw new RuntimeException("Dosnt have prop");
        if (key.sync == PropType.LOCAL_ONLY)
            localMap.put(key, key.type.cast(value));
        else {
            dataMap.put(key, key.type.cast(value));
            if (key.sync == PropType.ACTIVE_SYNC)
                needSync = true;
        }
    }

    public transient boolean needSync = true;

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

    public transient double timeDelta = 0;

    /**
     * ID of this entity.
     */
    public final long id;


    public Entity(double x, double y, Hitbox hitbox, CollisionStrat collisionStrat, Pair<EntityProperty, Object>... varargs) {
        this.add(X, x);
        this.add(Y, y);
        this.add(HITBOX, hitbox);
        this.add(COLLISION_STRAT, collisionStrat);
        id = Util.random.nextLong();
        for (Pair<EntityProperty, Object> pair : varargs)
            add(pair);

        if (this.has(AI_STRAT))
            this.<AIStrat>get(AI_STRAT).init(this);
        if (this.has(UPDATE_STRAT))
            this.<UpdateStrat>get(UPDATE_STRAT).init(this);
    }

    /**
     * Default constructor needed for serialization.
     */
    private Entity() {
        id = 0;
    }

    public void updateAI(ServerGameModel model, double secondsElapsed) {
        this.<AIStrat>oget(AI_STRAT).ifPresent(strat -> strat.updateAI(this, model, secondsElapsed));
    }

    public void draw(GraphicsContext gc) {
        this.<DrawStrat>oget(DRAW_STRAT).ifPresent(strat ->

                strat.draw(this, gc));

        if (!this.invincible()) {
            //TEMPORARY!!!!!!!!!
            gc.setFill(Color.RED);
            gc.fillRect(toDrawCoords(getX()) - 10, toDrawCoords(getX()) - 30, 20, 3);
            gc.setFill(Color.GREEN);
            gc.fillRect(toDrawCoords(getX()) - 10, toDrawCoords(getX()) - 30, (getHealth() / 100) * 20, 3);
        }
    }

    public Hitbox getHitbox() {
        return this.get(HITBOX);
    }

    public Double getX() {
        return this.get(X);
    }

    public Double getY() {
        return this.get(Y);
    }

    public void setX(double x) {
        this.set(X, x);
    }

    public void translateX(double d) {
        if(d!= 0)
        this.setX(this.getX() + d);
    }

    public void setY(double y) {
        this.set(X, y);
    }

    public void translateY(double d) {
        if(d != 0)
        this.setY(this.getY() + d);
    }

    private boolean invincible() {
        return !this.has(HEALTH);
    }

    public double getDrawZ() {
        return this.<DrawStrat>get(DRAW_STRAT).getDrawZ(this);
    }

    public long lastUpdate() {
        return this.get(LAST_UPDATE_TIME);
    }

    public Team getTeam() {
        return this.get(TEAM);
    }

    public Role getRole() {
        return this.get(ROLE);
    }

    public Velocity getVelocity() {
        return this.get(VELOCITY);
    }

    public void setVelocity(Velocity velocity) {
        this.set(VELOCITY, velocity);
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

    public void update(GameModel model, long modelCurrentTime) {
        inCollision = false;

        this.<UpdateStrat>oget(UPDATE_STRAT).ifPresent(updateStrat -> {

            if (!this.has(LAST_UPDATE_TIME)) {
                this.add(LAST_UPDATE_TIME, modelCurrentTime);
                return;
            }

            this.timeDelta = (modelCurrentTime - this.<Long>get(LAST_UPDATE_TIME)) * Const.NANOS_TO_SECONDS;
            this.set(LAST_UPDATE_TIME, modelCurrentTime);


            updateStrat.update(this, model);});
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
        return this.get(HEALTH);
    }

    public void onDeath(ServerCallBack deathHandler) {
        serverDeathCallBacks.add(deathHandler);
    }
}
