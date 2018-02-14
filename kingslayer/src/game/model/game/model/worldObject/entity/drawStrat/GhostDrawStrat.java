package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import game.model.game.model.worldObject.entity.collideStrat.GhostCollisionStrat;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static util.Util.toDrawCoords;

public class GhostDrawStrat extends ImageDrawStrat {

  public static final WallGhostDrawStrat GHOSTWALL = new WallGhostDrawStrat();
  public static final ResourceCollectorRedGhostDrawStrat GHOST_RED_COLLECTOR = new ResourceCollectorRedGhostDrawStrat();
  public static final ResourceCollectorBlueGhostDrawStrat GHOST_BLUE_COLLECTOR = new ResourceCollectorBlueGhostDrawStrat();

  @Override
  public DrawData initDrawData() {
    return null;
  }

  @Override
  public Image getImage() {
    return null;
  }

  @Override
  double getWidth() {
    return 1.0;
  }

  @Override
  double getHeight() {
    return 1.75;
  }

  @Override
  double getCenterX() {
    return 0.5;
  }

  @Override
  double getCenterY() {
    return 0.5;
  }

  @Override
  public void draw(Entity entity, GraphicsContext gc) {
    double w = this.getWidth();
    double h = this.getHeight();
    double x = w - this.getCenterX();
    double y = h - this.getCenterY();

    gc.setGlobalAlpha(0.5);
    gc.drawImage(getImage(),
        toDrawCoords(entity.data.x - x),
        toDrawCoords(entity.data.y - y),
        toDrawCoords(w),
        toDrawCoords(h));
    gc.setGlobalAlpha(1.0);
  }

  @Override
  public double getDrawZ(EntityData entity) {
    return entity.y;
  }

  public static class WallGhostDrawStrat extends GhostDrawStrat {
    @Override
    public Image getImage() {
      return Images.WALL_BUILDABLE_IMAGE;
    }

    private WallGhostDrawStrat() {
    }
  }

  public static class ResourceCollectorRedGhostDrawStrat extends GhostDrawStrat {
    @Override
    public Image getImage() {
      return Images.RED_RESOURCE_COLLECTOR_IMAGE;
    }

    private ResourceCollectorRedGhostDrawStrat() {
    }
  }

  public static class ResourceCollectorBlueGhostDrawStrat extends GhostDrawStrat {
    @Override
    public Image getImage() {
      return Images.BLUE_RESOURCE_COLLECTOR_IMAGE;
    }

    private ResourceCollectorBlueGhostDrawStrat() {
    }
  }
}
