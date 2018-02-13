package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import game.model.game.model.worldObject.entity.collideStrat.GhostCollisionStrat;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static util.Util.toDrawCoords;

public class GhostDrawStrat extends DrawStrat {

  public static final WallGhostDrawStrat ghostWall = new WallGhostDrawStrat();

  private int width = 32;
  private int height = 32;

  @Override
  public DrawData initDrawData() {
    return null;
  }

  public Image getImage() {
    return null;
  }

  @Override
  public void draw(Entity entity, GraphicsContext gc) {
    gc.setGlobalAlpha(0.2);
    gc.drawImage(getImage(),
        toDrawCoords(entity.data.x - entity.data.hitbox.getWidth() / 2),
        toDrawCoords(entity.data.y - entity.data.hitbox.getHeight() / 2),
        toDrawCoords(entity.data.hitbox.getWidth()),
        toDrawCoords(entity.data.hitbox.getHeight()));
    gc.setGlobalAlpha(1.0);
  }

  @Override
  public double getDrawZ(EntityData entity) {
    return 0;
  }

  public static class WallGhostDrawStrat extends GhostDrawStrat {
    @Override
    public Image getImage() {
      return Images.WALL_BUILDABLE_IMAGE;
    }
  }
}
