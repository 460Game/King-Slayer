package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static util.Util.toDrawCoords;

public class UpgradableImageDrawStrat extends ImageDrawStrat {

  public static final UpgradableImageDrawStrat BUILDABLE_WOOD_WALL = new BuildableWall();

  public static final UpgradableImageDrawStrat RESOURCE_COLLECTOR = new ResourceCollectorImageDrawStrat();
  public static final UpgradableImageDrawStrat BARRACKS = new BarracksImageDrawStrat();
  public static final UpgradableImageDrawStrat ARROW_TOWER = new ArrowTowerImageDrawStrat();

  public UpgradableImageDrawStrat() {}

  @Override
  public void draw(Entity entity, ClientGameModel gameModel, GraphicsContext gc) {
    double w = this.getWidth();
    double h = this.getHeight();
    double x = w - this.getCenterX();
    double y = h - this.getCenterY();

    gc.drawImage(getImage(entity),
        toDrawCoords(w) * entity.<Integer>getOrDefault(Entity.EntityProperty.LEVEL, 0),
        0,
        toDrawCoords(w),
        toDrawCoords(h),
        toDrawCoords(entity.getX() - x),
        toDrawCoords(entity.getY() - y),
        toDrawCoords(w),
        toDrawCoords(h));
  }

  @Override
  Image getImage(Entity entity) {
    return null;
  }

  @Override
  public double getWidth() {
    return 1;
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

  public static class BuildableWall extends UpgradableImageDrawStrat {
    @Override
    public Image getImage(Entity entity) {
      return Images.WALLS_BUILDABLE_IMAGE;
    }

    private BuildableWall() {}
  }

  public static class ResourceCollectorImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    public Image getImage(Entity entity) {
      switch (entity.getTeam()) {
        case RED_TEAM:
          return Images.RED_RESOURCE_COLLECTORS_IMAGE;
        case BLUE_TEAM:
          return Images.BLUE_RESOURCE_COLLECTORS_IMAGE;
      }
      return null;
    }

    private ResourceCollectorImageDrawStrat() {}
  }

  public static class BarracksImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    public Image getImage(Entity entity) {
      switch (entity.getTeam()) {
        case RED_TEAM:
          return Images.RED_BARRACKS_IMAGE;
        case BLUE_TEAM:
          return Images.BLUE_BARRACKS_IMAGE;
      }
      return null;
    }

    private BarracksImageDrawStrat() {}
  }

  public static class ArrowTowerImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    public Image getImage(Entity entity) {
      switch (entity.getTeam()) {
        case RED_TEAM:
          return Images.RED_ARROW_TOWER_IMAGE;
        case BLUE_TEAM:
          return Images.BLUE_ARROW_TOWER_IMAGE;
      }
      return null;
    }

    private ArrowTowerImageDrawStrat() {}
  }
}
