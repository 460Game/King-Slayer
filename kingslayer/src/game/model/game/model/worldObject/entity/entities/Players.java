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

    public static Entity makeSlayerA(Double x, Double y) {
        return new Entity(x, y,
            Team.ONE,
            Role.SLAYER,
            MovingStrat.SINGLETON,
            UnitCollisionStrat.SINGLETON,
            hitbox,
            DirectionAnimationDrawStrat.RED_SLAYER_ANIMATION, //TODO drawForeground strat
            AIDoNothingStrat.SINGLETON,
                SlayerDeathStrat.SINGLETON);
    }

    public static Entity makeSlayerB(Double x, Double y) {
        return new Entity(x, y,
            Team.TWO,
            Role.SLAYER,
            MovingStrat.SINGLETON,
            UnitCollisionStrat.SINGLETON,
            hitbox,
            DirectionAnimationDrawStrat.BLUE_SLAYER_ANIMATION, //TODO drawForeground strat
            AIDoNothingStrat.SINGLETON,
                SlayerDeathStrat.SINGLETON);
    }

    public static Entity makeKingA(double x, double y) {
        return new Entity(x, y,
            Team.ONE,
            Role.KING,
            MovingStrat.SINGLETON,
            UnitCollisionStrat.SINGLETON,
            hitbox,
            DirectionAnimationDrawStrat.RED_KING_ANIMATION, //TODO drawForeground strat
            AIDoNothingStrat.SINGLETON,
                KingDeathStrat.SINGLETON);
    }

    public static Entity makeKingB(double x, double y) {
        return new Entity(x, y,
            Team.TWO,
            Role.KING,
            MovingStrat.SINGLETON,
            UnitCollisionStrat.SINGLETON,
            hitbox,
            DirectionAnimationDrawStrat.BLUE_KING_ANIMATION, //TODO drawForeground strat
            AIDoNothingStrat.SINGLETON,
                KingDeathStrat.SINGLETON);
    }
}
