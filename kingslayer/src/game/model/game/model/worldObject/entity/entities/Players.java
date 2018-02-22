package game.model.game.model.worldObject.entity.entities;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.UnitCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.deathStrat.KingDeathStrat;
import game.model.game.model.worldObject.entity.deathStrat.SlayerDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.*;
import static util.Pair.pair;

public class Players {

    private static final double PLAYER_RADIUS = 0.35;
    
    static private CircleHitbox hitbox = new CircleHitbox(PLAYER_RADIUS);

    // TODO scale health appropriately

    public static Entity makeSlayer(Double x, Double y, Team team) {
        Log.info("Makeing slayer " + x + " " + y + " " + team);
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(HEALTH, 100.0),
            pair(TEAM, team),
            pair(ROLE, Role.SLAYER),
            pair(UPDATE_STRAT, MovingStrat.SINGLETON),
            pair(DRAW_STRAT, DirectionAnimationDrawStrat.SLAYER_ANIMATION),
               pair(DEATH_STRAT,  SlayerDeathStrat.SINGLETON));
    }

    public static Entity makeKing(double x, double y, Team team) {
        Log.info("Makeing king " + x + " " + y + " " + team);
        return new Entity(x, y,
                hitbox,
                UnitCollisionStrat.SINGLETON,
                pair(HEALTH, 100.0),
                pair(TEAM, team),
                pair(ROLE, Role.KING),
                pair(UPDATE_STRAT, MovingStrat.SINGLETON),
                pair(DRAW_STRAT, DirectionAnimationDrawStrat.KING_ANIMATION),
                pair(DEATH_STRAT,  KingDeathStrat.SINGLETON));
    }
}
