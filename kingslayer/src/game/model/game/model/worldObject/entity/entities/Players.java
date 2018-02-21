package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.collideStrat.UnitCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.deathStrat.KingDeathStrat;
import game.model.game.model.worldObject.entity.deathStrat.SlayerDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

public class Players {

    private static final double PLAYER_RADIUS = 0.35;
    
    static private CircleHitbox hitbox = new CircleHitbox(PLAYER_RADIUS);

    // TODO scale health appropriately

    public static Entity makeSlayer(Double x, Double y, Team team) {
        return new Entity(x, y, 100,
            team,
            Role.SLAYER,
            MovingStrat.SINGLETON,
            UnitCollisionStrat.SINGLETON,
            hitbox,
            DirectionAnimationDrawStrat.SLAYER_ANIMATION, //TODO drawForeground strat
            AIDoNothingStrat.SINGLETON,
                SlayerDeathStrat.SINGLETON);
    }

    public static Entity makeKing(double x, double y, Team team) {
        return new Entity(x, y, 100,
            team,
            Role.KING,
            MovingStrat.SINGLETON,
            UnitCollisionStrat.SINGLETON,
            hitbox,
            DirectionAnimationDrawStrat.KING_ANIMATION, //TODO drawForeground strat
            AIDoNothingStrat.SINGLETON,
                KingDeathStrat.SINGLETON);
    }
}
