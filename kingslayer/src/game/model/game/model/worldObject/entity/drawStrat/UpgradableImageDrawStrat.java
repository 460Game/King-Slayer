package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static util.Util.toDrawCoords;

public class UpgradableImageDrawStrat extends ImageDrawStrat {

  public static final UpgradableImageDrawStrat BUILDABLE_WOOD_WALL = new BuildableWall(0);
  public static final UpgradableImageDrawStrat WOOD_RESOURCE_COLLECTOR = new ResourceCollectorImageDrawStrat(0);
  public static final UpgradableImageDrawStrat WOOD_BARRACKS = new BarracksImageDrawStrat(0);
  public static final UpgradableImageDrawStrat WOOD_ARROW_TOWER = new ArrowTowerImageDrawStrat(0);

  public static final UpgradableImageDrawStrat STONE_RESOURCE_COLLECTOR = new ResourceCollectorImageDrawStrat(1);
  public static final UpgradableImageDrawStrat STONE_BARRACKS = new BarracksImageDrawStrat(1);
  public static final UpgradableImageDrawStrat STONE_ARROW_TOWER = new ArrowTowerImageDrawStrat(1);

  int tier;

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
        toDrawCoords(w) * tier,
        0,
        toDrawCoords(w),
        toDrawCoords(h),
        toDrawCoords(entity.data.x - x),
        toDrawCoords(entity.data.y - y),
        toDrawCoords(w),
        toDrawCoords(h));
  }

  @Override
  public void upgrade(GameModel model) {
    System.out.println("upgrading tier of texture");
    if (tier < 2)
      tier++;
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

    private BuildableWall(int tier) {
      this.tier = tier;
    }
  }

  public static class ResourceCollectorImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    public Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_RESOURCE_COLLECTORS_IMAGE;
        case TWO:
          return Images.BLUE_RESOURCE_COLLECTORS_IMAGE;
        default:
          return null;
      }
    }

    private ResourceCollectorImageDrawStrat() {}

    private ResourceCollectorImageDrawStrat(int tier) {
      this.tier = tier;
    }
  }

  public static class BarracksImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    public Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_BARRACKS_IMAGE;
        case TWO:
          return Images.BLUE_BARRACKS_IMAGE;
        default:
          return null;
      }
    }

    private BarracksImageDrawStrat() {}

    private BarracksImageDrawStrat(int tier) {
      this.tier = tier;
    }
  }

  public static class ArrowTowerImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    public Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_ARROW_TOWER_IMAGE;
        case TWO:
          return Images.BLUE_ARROW_TOWER_IMAGE;
        default:
          return null;
      }
    }

    private ArrowTowerImageDrawStrat() {}

    private ArrowTowerImageDrawStrat(int tier) {
      this.tier = tier;
    }
  }
}
