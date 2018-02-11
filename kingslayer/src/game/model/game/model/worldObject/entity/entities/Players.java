package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.collideStrat.UnitCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

public class Players {

    private static final double PLAYER_RADIUS = 0.3;
    
    static private CircleHitbox hitbox = new CircleHitbox(PLAYER_RADIUS);

    public static Entity makeSlayer(Double x, Double y) {
        return new Entity(x, y,
            Team.ONE,
            MovingStrat.SINGLETON,
            UnitCollisionStrat.SINGLETON,
            hitbox,
            DirectionAnimationDrawStrat.RED_SLAYER_ANIMATION, //TODDO drawFG strat
            AIDoNothingStrat.SINGLETON);
    }

    public static Entity makeKing(Double x, Double y) {
        return new Entity(x, y,
            Team.ONE,
            MovingStrat.SINGLETON,
            UnitCollisionStrat.SINGLETON,
            hitbox,
            DirectionAnimationDrawStrat.RED_KING_ANIMATION, //TODDO drawFG strat
            AIDoNothingStrat.SINGLETON);
    }
}
