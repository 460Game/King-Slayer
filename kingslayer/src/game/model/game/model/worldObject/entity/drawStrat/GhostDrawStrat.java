package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static util.Util.toDrawCoords;

public class GhostDrawStrat extends ImageDrawStrat {

  public static final GhostDrawStrat GHOSTWALL = new WallGhostDrawStrat();
  public static final GhostDrawStrat GHOST_COLLECTOR = new ResourceCollectorGhostDrawStrat();
  public static final GhostDrawStrat GHOST_BARRACKS = new BarracksGhostDrawStrat();
  public static final GhostDrawStrat GHOST_ARROW_TOWER = new ArrowTowerGhostDrawStrat();

  public boolean invalidLocation = false;

  @Override
  public DrawData initDrawData() {
    return null;
  }

  @Override
  public Image getImage(Entity entity) {
    return null;
  }

  @Override
  public double getWidth() {
    return 1.0;
  }

  @Override
  public double getHeight() {
    return 1.625;
  }

  @Override
  public double getCenterX() {
    return 0.5;
  }

  @Override
  public double getCenterY() {
    return 0.5;
  }

  @Override
  public void draw(Entity entity, ClientGameModel model, GraphicsContext gc) {
    double w = this.getWidth();
    double h = this.getHeight();
    double x = w - this.getCenterX();
    double y = h - this.getCenterY();

    gc.setGlobalAlpha(0.5);
    gc.drawImage(getImage(entity),
        toDrawCoords(entity.getX() - x),
        toDrawCoords(entity.getY() - y),
        toDrawCoords(w),
        toDrawCoords(h));
    gc.setGlobalAlpha(1.0);

    if (entity.inCollision || invalidLocation) {
      gc.setFill(new Color(1.0, 0.0, 0.0, 0.35));
      gc.fillRect(toDrawCoords(entity.getX() - x),
          toDrawCoords(entity.getY() - y),
          toDrawCoords(w),
          toDrawCoords(h));
    }
  }

  @Override
  public double getDrawZ(Entity entity) {
    return entity.getY();
  }

  public static class WallGhostDrawStrat extends GhostDrawStrat {
    @Override
    public Image getImage(Entity entity) {
      return Images.WALL_BUILDABLE_IMAGE;
    }

    private WallGhostDrawStrat() {
    }
  }

  public static class ResourceCollectorGhostDrawStrat extends GhostDrawStrat {
    public Image getImage(Entity entity) {
      switch (entity.getTeam()) {
        case RED_TEAM:
          return Images.RED_RESOURCE_COLLECTOR_IMAGE;
        case BLUE_TEAM:
          return Images.BLUE_RESOURCE_COLLECTOR_IMAGE;
        default:
          return null;
      }
    }

    private ResourceCollectorGhostDrawStrat() {
    }
  }

  public static class BarracksGhostDrawStrat extends GhostDrawStrat {
    public Image getImage(Entity entity) {
      switch (entity.getTeam()) {
        case RED_TEAM:
          return Images.RED_WOOD_BARRACKS_IMAGE;
        case BLUE_TEAM:
          return Images.BLUE_WOOD_BARRACKS_IMAGE;
        default:
          return null;
      }
    }

    private BarracksGhostDrawStrat() {
    }
  }

  public static class ArrowTowerGhostDrawStrat extends GhostDrawStrat {
    public Image getImage(Entity entity) {
      switch (entity.getTeam()) {
        case RED_TEAM:
          return Images.RED_WOOD_ARROW_TOWER_IMAGE;
        case BLUE_TEAM:
          return Images.BLUE_WOOD_ARROW_TOWER_IMAGE;
        default:
          return null;
      }
    }

    private ArrowTowerGhostDrawStrat() {
    }
  }
}
