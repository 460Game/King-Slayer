package game.model.game.model.worldObject.entity.updateStrat;

import game.message.toServer.MakeEntityRequest;
import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Minions;
import javafx.util.Pair;

import java.util.function.Function;

public class SpawningStrat extends UpdateStrat {
  int counter;
  int maxSpawns;
  int spawnCounter;
  int timeBetweenSpawns;
  Function<Pair<Integer, Integer>, Entity> function;

  public SpawningStrat() {
    
  }

  public SpawningStrat(int timeBetweenSpawns, int maxSpawns, Function<Pair<Integer, Integer>, Entity> function) {
    this.counter = 0;
    this.timeBetweenSpawns = timeBetweenSpawns;
    this.maxSpawns = maxSpawns;
    this.spawnCounter = 0;
    this.function = function;
  }

  @Override
  protected void update(Entity entity, GameModel model, double seconds) {
    counter++;
    if (counter > timeBetweenSpawns) {
      if (spawnCounter < maxSpawns) {
        model.processMessage(new MakeEntityRequest(Minions.makeRangedMinionOne(entity.data.x, entity.data.y + 1),
            Team.ONE, TeamResourceData.Resource.WOOD, 0));
        spawnCounter++;
      }
      counter = 0;
    }
  }
}
