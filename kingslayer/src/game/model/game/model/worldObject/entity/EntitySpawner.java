package game.model.game.model.worldObject.entity;

import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.entities.Entities;

public enum EntitySpawner {
  WALL_SPAWNER(TeamResourceData.Resource.WOOD, -10) {
    @Override
    public Entity makeEntity(Team team) {
      return Entities.makeBuiltWall(x, y, team);
    }
  },
  RESOURCE_COLLETOR_SPAWNER(TeamResourceData.Resource.WOOD, -10) {
    @Override
    public Entity makeEntity(Team team) {
      return Entities.makeResourceCollector(x, y, team);
    }
  },
  MELEE_BARRACKS_SPAWNER(TeamResourceData.Resource.WOOD, -10) {
    @Override
    public Entity makeEntity(Team team) {
      return Entities.makeMeleeBarracks(x, y, team);
    }
  },
  RANGED_BARRACKS_SPAWNER(TeamResourceData.Resource.WOOD, -10) {
    @Override
    public Entity makeEntity(Team team) {
      return Entities.makeRangedBarracks(x, y, team);
    }
  },
  SIEGE_BARRACKS_SPAWNER(TeamResourceData.Resource.WOOD, -10) {
    @Override
    public Entity makeEntity(Team team) {
      return Entities.makeSiegeBarracks(x, y, team);
    }
  },
  EXPLORATION_BARRACKS_SPAWNER(TeamResourceData.Resource.WOOD, -10) {
    @Override
    public Entity makeEntity(Team team) {
      return Entities.makeExplorationBarracks(x, y, team);
    }
  },
  ARROW_TOWER_SPAWNER(TeamResourceData.Resource.WOOD, -10) {
    @Override
    public Entity makeEntity(Team team) {
      return Entities.makeArrowTower(x, y, team);
    }
  };

  public TeamResourceData.Resource resource;
  public int cost;
  public double x;
  public double y;

  EntitySpawner(TeamResourceData.Resource resource, int cost) {
    this.resource = resource;
    this.cost = cost;
    this.x = 0;
    this.y = 0;
  }

  public abstract Entity makeEntity(Team team);

}
