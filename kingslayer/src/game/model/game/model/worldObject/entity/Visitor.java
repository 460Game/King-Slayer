package game.model.game.model.worldObject.entity;

import game.message.toServer.MakeEntityMessage;
import game.message.toServer.DeleteEntityMessage;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.entities.Entities;

import static util.Const.TILE_PIXELS;
import static util.Util.toDrawCoords;

public interface Visitor {
  public void run(Entity entity, ClientGameModel model);

  public class ShowPlacement implements Visitor {
    private double x;
    private double y;

    public ShowPlacement(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public void run(Entity entity, ClientGameModel model) {
      if (model.placing == null) {
        System.out.println("starting placement at " + x + " " + y);
//      ((DirectionAnimationDrawStrat) entity.drawStrat).turnOnPlacementBox(x, y);
        model.placing = Entities.makeBuiltWall(x / TILE_PIXELS, y / TILE_PIXELS);

        model.placingGhost = Entities.makeGhostWall(x / TILE_PIXELS, y / TILE_PIXELS);
        model.processMessage(new MakeEntityMessage(model.placingGhost, entity.team, TeamResourceData.Resource.WOOD, 0));
      }
    }
  }

  public class MoveEntity implements Visitor {
    private double x;
    private double y;

    private double sceneWidth;
    private double sceneHeight;

    public MoveEntity(double x, double y, double w, double h) {
      this.x = x;
      this.y = y;

      sceneWidth = w;
      sceneHeight = h;
    }

    public void run(Entity entity, ClientGameModel model) {
      if (model.placing != null) {
        System.out.println("moving to: " + x + " " + y);
//        ((DirectionAnimationDrawStrat) entity.drawStrat).movePlacementBox(x, y);
        model.placing = Entities.makeBuiltWall(Math.floor((toDrawCoords(entity.data.x) - sceneWidth / 2 + x) / TILE_PIXELS) + 0.5,
            Math.floor((toDrawCoords(entity.data.y) - sceneHeight / 2 + y) / TILE_PIXELS) + 0.5);

        model.processMessage(new DeleteEntityMessage(model.placingGhost));
        model.placingGhost.data.x = Math.floor((toDrawCoords(entity.data.x) - sceneWidth / 2 + x) / TILE_PIXELS) + 0.5;
        model.placingGhost.data.y = Math.floor((toDrawCoords(entity.data.y) - sceneHeight / 2 + y) / TILE_PIXELS) + 0.5;
        model.processMessage(new MakeEntityMessage(model.placingGhost, entity.team, TeamResourceData.Resource.WOOD, 0));
      }
    }
  }

  public class PlaceEntity implements Visitor {
    @Override
    public void run(Entity entity, ClientGameModel model) {
      if (model.placing != null) {
        // TODO check for possible placements
//        if (model.placing.data.hitbox.getCollidesWith(model, model.placing.data.x, model.placing.data.y).toArray().length == 0) {
          model.processMessage(new DeleteEntityMessage(model.placingGhost));
          model.processMessage(new MakeEntityMessage(model.placing, entity.team, TeamResourceData.Resource.WOOD, -10));
          model.placing = null;
//        }
      }
    }
  }

//  public class PlaceResourceCollector implements Visitor {
//    @Override
//    public void run(Entity entity, ClientGameModel model) {
//      ((DirectionAnimationDrawStrat) entity.drawStrat).turnOffPlacementBox();
//      double[] dir = {0, 0};
//      if (((DirectionAnimationDrawStrat) entity.drawStrat).drawData.direction == 'N')
//        dir[1] = -1;
//      else if (((DirectionAnimationDrawStrat) entity.drawStrat).drawData.direction == 'E')
//        dir[0] = 1;
//      else if (((DirectionAnimationDrawStrat) entity.drawStrat).drawData.direction == 'S')
//        dir[1] = 1;
//      else
//        dir[0] = -1;
//      if (entity.team == Team.ONE)
//        model.processMessage(new MakeEntityMessage(Entities.makeResourceCollectorRed(Math.floor(entity.data.x) + 0.5 + dir[0],
//            Math.floor(entity.data.y) + 0.5 + dir[1])));
//      else // TODO change to blue
//        model.processMessage(new MakeEntityMessage(Entities.makeResourceCollectorRed(Math.floor(entity.data.x) + 0.5 + dir[0],
//            Math.floor(entity.data.y) + 0.5 + dir[1])));
//    }
//  }
}
