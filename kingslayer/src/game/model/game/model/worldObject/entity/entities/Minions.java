package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.collideStrat.UnitCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.deathStrat.SlayerDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

public class Minions {

  private static final double MINION_RADIUS = 0.25;

  static private CircleHitbox hitbox = new CircleHitbox(MINION_RADIUS);

  public static Entity makeRangedMinionOne(double x, double y) {
    return new Entity(x, y, 100,
        Team.ONE,
        Role.NEUTRAL,
        MovingStrat.SINGLETON,
        UnitCollisionStrat.SINGLETON,
        hitbox,
        DirectionAnimationDrawStrat.RED_RANGED_ANIMATION,
        AIDoNothingStrat.SINGLETON,
        SlayerDeathStrat.SINGLETON);
  }

  public static Entity makeRangedMinionTwo(double x, double y) {
    return new Entity(x, y, 100,
        Team.TWO,
        Role.NEUTRAL,
        MovingStrat.SINGLETON,
        UnitCollisionStrat.SINGLETON,
        hitbox,
        DirectionAnimationDrawStrat.BLUE_RANGED_ANIMATION,
        AIDoNothingStrat.SINGLETON,
        SlayerDeathStrat.SINGLETON);
  }

}
