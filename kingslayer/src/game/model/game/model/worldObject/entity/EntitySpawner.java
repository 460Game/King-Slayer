package game.model.game.model.worldObject.entity;

import game.model.game.model.ClientGameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.aiStrat.BuildingSpawnerStrat;
import game.model.game.model.worldObject.entity.entities.Entities;

public enum EntitySpawner {
  WALL_SPAWNER(TeamResourceData.Resource.WOOD, 5) {
    @Override
    public Entity makeEntity(double x, double y, Team team) {
      return Entities.makeBuiltWall(x, y, team);
    }

    @Override
    public int finalCost(ClientGameModel model) {
      return cost;
    }

    @Override
    public int finalCost(ServerGameModel model, Team team) {
      return cost;
    }
  },
  RESOURCE_COLLETOR_SPAWNER(TeamResourceData.Resource.WOOD, 10) {
    @Override
    public Entity makeEntity(double x, double y, Team team) {
      return Entities.makeResourceCollector(x, y, team);
    }

    @Override
    public int finalCost(ClientGameModel model) {
      return cost + 2 * (int) model.getEntitiesOfType(BuildingSpawnerStrat.BuildingType.COLLECTOR)
            .filter(entity -> entity.getTeam() == model.getLocalPlayer().getTeam()).count();
    }

    @Override
    public int finalCost(ServerGameModel model, Team team) {
      return cost + 2 * (int) model.getEntitiesOfType(BuildingSpawnerStrat.BuildingType.COLLECTOR)
          .filter(entity -> entity.getTeam() == team).count();
    }
  },
  BARRACKS_SPAWNER(TeamResourceData.Resource.WOOD, 15) {
    @Override
    public Entity makeEntity(double x, double y, Team team) {
      return Entities.makeBarracks(x, y, team);
    }

    @Override
    public int finalCost(ClientGameModel model) {
      return cost * (int) Math.pow(2, (int) model.getEntitiesOfType(BuildingSpawnerStrat.BuildingType.BARRACKS)
            .filter(entity -> entity.getTeam() == model.getLocalPlayer().getTeam()).count());
    }

    @Override
    public int finalCost(ServerGameModel model, Team team) {
      return cost + (int) model.getEntitiesOfType(BuildingSpawnerStrat.BuildingType.BARRACKS)
          .filter(entity -> entity.getTeam() == team).count();
    }
  },
  ARROW_TOWER_SPAWNER(TeamResourceData.Resource.WOOD, 50) {
    @Override
    public Entity makeEntity(double x, double y, Team team) {
      return Entities.makeArrowTower(x, y, team);
    }

    @Override
    public int finalCost(ClientGameModel model) {
      return cost;
    }

    @Override
    public int finalCost(ServerGameModel model, Team team) {
      return cost;
    }
  };

  public TeamResourceData.Resource resource;
  int cost;

  EntitySpawner(TeamResourceData.Resource resource, int cost) {
    this.resource = resource;
    this.cost = cost;
  }

  public abstract Entity makeEntity(double x, double y, Team team);

  public abstract int finalCost(ClientGameModel model);

  public abstract int finalCost(ServerGameModel model, Team team);

}
