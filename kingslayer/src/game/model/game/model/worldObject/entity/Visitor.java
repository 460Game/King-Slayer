package game.model.game.model.worldObject.entity;

import game.message.toClient.NewEntityMessage;
import game.model.game.model.Model;
import game.model.game.model.worldObject.entity.drawStrat.DirectionAnimationDrawStrat;
import game.model.game.model.worldObject.entity.entities.Entities;

public interface Visitor {
  public void run(Entity entity, Model model);

  public class ShowPlacement implements Visitor {
    public void run(Entity entity, Model model) {
      ((DirectionAnimationDrawStrat) entity.drawStrat).togglePlacementBox();
    }
  }

  public class PlaceWall implements Visitor {
    @Override
    public void run(Entity entity, Model model) {
      ((DirectionAnimationDrawStrat) entity.drawStrat).togglePlacementBox();
      double[] dir = {0, 0};
      if (((DirectionAnimationDrawStrat) entity.drawStrat).drawData.direction == 'N')
        dir[1] = -1;
      else if (((DirectionAnimationDrawStrat) entity.drawStrat).drawData.direction == 'E')
        dir[0] = 1;
      else if (((DirectionAnimationDrawStrat) entity.drawStrat).drawData.direction == 'S')
        dir[1] = 1;
      else
        dir[0] = -1;
      System.out.println("in entity: " + entity.data.x + " " + entity.data.y);
      model.processMessage(new NewEntityMessage(Entities.makeBuiltWall(Math.floor(entity.data.x) + 0.5 + dir[0],
          Math.floor(entity.data.y) + 0.5 + dir[1])));
    }
  }
}
