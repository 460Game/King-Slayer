package game.model.game.model.worldObject.entity;

import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.aiStrat.AIStrat;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import game.model.game.model.worldObject.entity.collideStrat.hitbox.Hitbox;
import game.model.game.model.worldObject.entity.deathStrat.DeathStrat;
import game.model.game.model.worldObject.entity.drawStrat.DrawStrat;
import game.model.game.model.worldObject.entity.updateStrat.UpdateStrat;

public class GhostEntity extends Entity {
  /**
   * Constructor of an entity, given all of its game data.
   *
   * @param x
   *     x-coordinate of the center of its position
   * @param y
   *     y-coordinate of the center of its position
   * @param team
   *     team corresponding to this entity
   * @param role
   * @param updateStrat
   *     method with which this entity updates
   * @param collisionStrat
   *     method with which this entity collides.
   * @param hitbox
   *     hitbox of this entity
   * @param drawStrat
   *     method with which this entity is drawn
   * @param aiStrat
   *     TODO
   */
  public GhostEntity(double x,
                     double y,
                     Team team,
                     Role role,
                     UpdateStrat updateStrat,
                     CollisionStrat collisionStrat,
                     Hitbox hitbox,
                     DrawStrat drawStrat,
                     AIStrat aiStrat,
                     DeathStrat deathStrat) {
    super(x, y, team, role, updateStrat, collisionStrat, hitbox, drawStrat, aiStrat, deathStrat);
  }
}
