package game.model.game.model.worldObject.entity;

import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.entities.Entities;
import javafx.util.Pair;

import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class EntitySpawner {

//  public String type;
  public BiFunction<Pair<Double, Double>, Team, Entity> function;
  public TeamResourceData.Resource resource;
  public int cost;
  public double x;
  public double y;

  public EntitySpawner() {}

//  public EntitySpawner(String type, TeamResourceData.Resource resource, int cost, double x, double y) {
//    this.type = type;
//    this.resource = resource;
//    this.cost = cost;
//    this.x = x;
//    this.y = y;
//  }

  public EntitySpawner(BiFunction<Pair<Double, Double>, Team, Entity> function, TeamResourceData.Resource resource, int cost, double x, double y) {
    this.function = function;
    this.resource = resource;
    this.cost = cost;
    this.x = x;
    this.y = y;
  }

  public Entity makeEntity(Team team) {
    return function.apply(new Pair<Double, Double>(x, y), team);
//    switch (type) {
//      case "arrow_tower":
//        return Entities.makeArrowTower(x, y, team);
//      default:
//        return null;
//    }
  }

}
