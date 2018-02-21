package game.model.game.model.worldObject.entity.entities;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.aiStrat.AIDoNothingStrat;
import game.model.game.model.worldObject.entity.aiStrat.MinionStrat;
import game.model.game.model.worldObject.entity.collideStrat.UnitCollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.CircleHitbox;
import game.model.game.model.worldObject.entity.deathStrat.RemoveOnDeathStrat;
import game.model.game.model.worldObject.entity.deathStrat.SlayerDeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.MovingStrat;

public class Minions {

  private static final double MINION_RADIUS = 0.25;

  static private CircleHitbox hitbox = new CircleHitbox(MINION_RADIUS);

    public static Entity makeRangedMinion(double x, double y, Team team) {
      return new Entity(x, y, 100,
              team,
              Role.NEUTRAL,
              MovingStrat.SINGLETON,
              UnitCollisionStrat.SINGLETON,
              hitbox,
              DirectionAnimationDrawStrat.RANGED_ANIMATION,
              MinionStrat.RangedMinionStrat.SINGLETON,
              RemoveOnDeathStrat.SINGLETON);
    }

  public static Entity makeMeleeMinion(double x, double y, Team team) {
    return new Entity(x, y, 100,
        team,
        Role.NEUTRAL,
        MovingStrat.SINGLETON,
        UnitCollisionStrat.SINGLETON,
        hitbox,
        DirectionAnimationDrawStrat.MELEE_ANIMATION,
        MinionStrat.RangedMinionStrat.SINGLETON, // TODO
        RemoveOnDeathStrat.SINGLETON);
  }

  public static Entity makeResourceMinion(double x, double y, Team team) {
    return new Entity(x, y, 100,
        team,
        Role.NEUTRAL,
        MovingStrat.SINGLETON,
        UnitCollisionStrat.SINGLETON,
        hitbox,
        DirectionAnimationDrawStrat.RESOURCE_MINION_ANIMATION,
        MinionStrat.RangedMinionStrat.SINGLETON, // TODO
        RemoveOnDeathStrat.SINGLETON);
  }
}
