package game.message.toServer;

import game.model.game.model.ServerGameModel;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;
import music.MusicPlayer;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.TEAM;

public class SellEntityRequest implements ToServerRequest {
  /**
   * Entity to be deleted.
   */
  private long id;
  int amount;

  private SellEntityRequest() {

  }

  /**
   * Constructor of the request, given an entity to be made.
   * @param entity entity to be made
   */
  public SellEntityRequest(Entity entity, int amount) {
    this.id = entity.id;
    this.amount = amount;
  }

  /**
   * Creates the entity in the server.
   * @param model the game model on the game server
   */
  @Override
  public void executeServer(ServerGameModel model) {
    Entity entity = model.getEntity(id);
    if (entity != null && entity.has(TEAM)) {
         model.changeResource(entity.getTeam(), TeamResourceData.levelToResource.get(entity.<Integer>get(Entity.EntityProperty.LEVEL)), amount);
        entity.entityDie(model);
    }
  }
}
