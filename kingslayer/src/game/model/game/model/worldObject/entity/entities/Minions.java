package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.MinionStrat;
import game.model.game.model.worldObject.entity.collideStrat.UnitCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.deathStrat.RemoveOnDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.AnimationDrawData;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.*;
import static util.Pair.pair;

public class Minions {

    private static final double MINION_RADIUS = 0.25;

    static private CircleHitbox hitbox = new CircleHitbox(MINION_RADIUS);

    public static Entity makeMeleeMinion(double x, double y, Team team, int level, Entity spawner) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(LEVEL, level),
                pair(SPAWNED_ID, spawner.id),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.MELEE_ANIMATION),
                pair(AI_STRAT, MinionStrat.MeleeMinionStrat.SINGLETON), // TODO what is this?
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(DRAW_DATA, AnimationDrawData.makeAnimated()),
                pair(SIGHT_RADIUS, 3),
                pair(HEALTH, 5.0),
                pair(MAX_HEALTH, 5.0),
                pair(MAX_SPEED, 1.0));
    }

    public static Entity makeRangedMinion(double x, double y, Team team, int level, Entity spawner) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(LEVEL, level),
                pair(SPAWNED_ID, spawner.id),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.RANGED_ANIMATION),
                pair(AI_STRAT, MinionStrat.RangedMinionStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(DRAW_DATA, AnimationDrawData.makeAnimated()),
                pair(SIGHT_RADIUS, 3),
                pair(HEALTH, 5.0),
                pair(MAX_HEALTH, 5.0),
                pair(MAX_SPEED, 1.0));
    }

    public static Entity makeSiegeMinion(double x, double y, Team team, int level, Entity spawner) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(LEVEL, level),
                pair(SPAWNED_ID, spawner.id),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.SIEGE_ANIMATION),
                pair(AI_STRAT, MinionStrat.RangedMinionStrat.SINGLETON), // TODO
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(DRAW_DATA, AnimationDrawData.makeAnimated()),
                pair(SIGHT_RADIUS, 3),
                pair(HEALTH, 5.0),
                pair(MAX_HEALTH, 5.0),
                pair(MAX_SPEED, 1.0));
    }

    public static Entity makeExplorationMinion(double x, double y, Team team, int level, Entity spawner) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(TEAM, team),
                pair(LEVEL, level),
                pair(SPAWNED_ID, spawner.id),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.EXPLORATION_ANIMATION),
                pair(AI_STRAT, MinionStrat.RangedMinionStrat.SINGLETON), // TODO
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(DRAW_DATA, AnimationDrawData.makeAnimated()),
                pair(SIGHT_RADIUS, 3),
                pair(HEALTH, 10.0),
                pair(MAX_HEALTH, 10.0),
                pair(MAX_SPEED, 3.0));
    }

    public static Entity makeResourceMinion(double x, double y, Team team, int level, Entity spawner) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(LEVEL, level),
                pair(TEAM, team),
                pair(SPAWNED_ID, spawner.id),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.RESOURCE_MINION_ANIMATION),
                pair(AI_STRAT, MinionStrat.ResourceMinionStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON),
                pair(DRAW_DATA, AnimationDrawData.makeAnimated()),
                pair(SIGHT_RADIUS, 3),
                pair(HEALTH, 5.0),
                pair(MAX_HEALTH, 5.0),
                pair(MAX_SPEED, 1.0));
    }
}
