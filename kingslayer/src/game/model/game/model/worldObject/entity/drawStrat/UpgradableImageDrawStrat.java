package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static util.Util.toDrawCoords;

public class UpgradableImageDrawStrat extends ImageDrawStrat {

  public static final UpgradableImageDrawStrat BUILDABLE_WOOD_WALL = new BuildableWall();
  public static final UpgradableImageDrawStrat WOOD_RESOURCE_COLLECTOR = new ResourceCollectorImageDrawStrat();
  public static final UpgradableImageDrawStrat WOOD_BARRACKS = new BarracksImageDrawStrat();
  public static final UpgradableImageDrawStrat WOOD_ARROW_TOWER = new ArrowTowerImageDrawStrat();

  public static final UpgradableImageDrawStrat STONE_RESOURCE_COLLECTOR = new ResourceCollectorImageDrawStrat();
  public static final UpgradableImageDrawStrat STONE_BARRACKS = new BarracksImageDrawStrat();
  public static final UpgradableImageDrawStrat STONE_ARROW_TOWER = new ArrowTowerImageDrawStrat();

  public UpgradableImageDrawStrat() {}

  public Image getImage(Entity entity) {
    return null;
  }

  @Override
  public void draw(Entity entity, GraphicsContext gc) {
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
  Image getImage() {
    return null;
  }

  @Override
  double getWidth() {
    return 1;
  }

  @Override
  double getHeight() {
    return 1.625;
  }

  @Override
  double getCenterX() {
    return 0.5;
  }

  @Override
  double getCenterY() {
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
        case ONE:
          return Images.RED_RESOURCE_COLLECTORS_IMAGE;
        case TWO:
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
        case ONE:
          return Images.RED_BARRACKS_IMAGE;
        case TWO:
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
        case ONE:
          return Images.RED_ARROW_TOWER_IMAGE;
        case TWO:
          return Images.BLUE_ARROW_TOWER_IMAGE;
      }
      return null;
    }

    private ArrowTowerImageDrawStrat() {}
  }
}
