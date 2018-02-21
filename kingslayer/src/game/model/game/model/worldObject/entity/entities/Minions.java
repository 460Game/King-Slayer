package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.MinionStrat;
import game.model.game.model.worldObject.entity.collideStrat.UnitCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.deathStrat.RemoveOnDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.*;
import static util.Pair.pair;

public class Minions {

    private static final double MINION_RADIUS = 0.25;

    static private CircleHitbox hitbox = new CircleHitbox(MINION_RADIUS);

    public static Entity makeMeleeMinion(double x, double y, Team team) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(HEALTH, 100),
                pair(TEAM, team),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.MELEE_ANIMATION),
                pair(AI_STRAT, MinionStrat.RangedMinionStrat.SINGLETON), // TODO what is this?
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeRangedMinion(double x, double y, Team team) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(HEALTH, 100),
                pair(TEAM, team),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.RANGED_ANIMATION),
                pair(AI_STRAT, MinionStrat.RangedMinionStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeSiegeMinion(double x, double y, Team team) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(HEALTH, 100),
                pair(TEAM, team),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.SIEGE_ANIMATION),
                pair(AI_STRAT, MinionStrat.RangedMinionStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }

    public static Entity makeResourceMinion(double x, double y, Team team) {
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(HEALTH, 100),
                pair(TEAM, team),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.RESOURCE_MINION_ANIMATION),
                pair(AI_STRAT, MinionStrat.RangedMinionStrat.SINGLETON),
                pair(DEATH_STRAT, RemoveOnDeathStrat.SINGLETON));
    }
}
