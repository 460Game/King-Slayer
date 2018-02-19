package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static util.Util.toDrawCoords;

public class UpgradableImageDrawStrat extends ImageDrawStrat {

  public static final UpgradableImageDrawStrat BUILDABLE_WOOD_WALL = new BuildableWall(0);
  public static final UpgradableImageDrawStrat RED_WOOD_RESOURCE_COLLECTOR = new RedResourceCollectorImageDrawStrat(0);
  public static final UpgradableImageDrawStrat BLUE_WOOD_RESOURCE_COLLECTOR = new BlueResourceCollectorImageDrawStrat(0);
  public static final UpgradableImageDrawStrat RED_WOOD_RANGED_BARRACKS = new RedRangedBarracksImageDrawStrat(0);
  public static final UpgradableImageDrawStrat BLUE_WOOD_RANGED_BARRACKS = new BlueRangedBarracksImageDrawStrat(0);
  public static final UpgradableImageDrawStrat RED_WOOD_ARROW_TOWER = new RedArrowTowerImageDrawStrat(0);
  public static final UpgradableImageDrawStrat BLUE_WOOD_ARROW_TOWER = new BlueArrowTowerImageDrawStrat(0);

  public static final UpgradableImageDrawStrat RED_STONE_RESOURCE_COLLECTOR = new RedResourceCollectorImageDrawStrat(1);
  public static final UpgradableImageDrawStrat BLUE_STONE_RESOURCE_COLLECTOR = new BlueResourceCollectorImageDrawStrat(1);
  public static final UpgradableImageDrawStrat RED_STONE_RANGED_BARRACKS = new RedRangedBarracksImageDrawStrat(1);
  public static final UpgradableImageDrawStrat BLUE_STONE_RANGED_BARRACKS = new BlueRangedBarracksImageDrawStrat(1);
  public static final UpgradableImageDrawStrat RED_STONE_ARROW_TOWER = new RedArrowTowerImageDrawStrat(1);
  public static final UpgradableImageDrawStrat BLUE_STONE_ARROW_TOWER = new BlueArrowTowerImageDrawStrat(1);

  int tier;

  public UpgradableImageDrawStrat() {}

  @Override
  public void draw(Entity entity, GraphicsContext gc) {
    double w = this.getWidth();
    double h = this.getHeight();
    double x = w - this.getCenterX();
    double y = h - this.getCenterY();

    gc.drawImage(getImage(),
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
    Image getImage() {
      return Images.WALLS_BUILDABLE_IMAGE;
    }

    private BuildableWall() {}

    private BuildableWall(int tier) {
      this.tier = tier;
    }
  }

  public static class RedResourceCollectorImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_RESOURCE_COLLECTORS_IMAGE;
    }

    private RedResourceCollectorImageDrawStrat() {}

    private RedResourceCollectorImageDrawStrat(int tier) {
      this.tier = tier;
    }
  }

  public static class BlueResourceCollectorImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_RESOURCE_COLLECTORS_IMAGE;
    }

    private BlueResourceCollectorImageDrawStrat() {}

    private BlueResourceCollectorImageDrawStrat(int tier) {

    }
  }

  public static class RedRangedBarracksImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_BARRACKS_IMAGE;
    }

    private RedRangedBarracksImageDrawStrat() {}

    private RedRangedBarracksImageDrawStrat(int tier) {
      this.tier = tier;
    }
  }

  public static class BlueRangedBarracksImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_BARRACKS_IMAGE;
    }

    private BlueRangedBarracksImageDrawStrat() {}

    private BlueRangedBarracksImageDrawStrat(int tier) {
      this.tier = tier;
    }
  }

  public static class RedArrowTowerImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_ARROW_TOWER_IMAGE;
    }

    private RedArrowTowerImageDrawStrat() {}

    private RedArrowTowerImageDrawStrat(int tier) {
      this.tier = tier;
    }
  }

  public static class BlueArrowTowerImageDrawStrat extends UpgradableImageDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_ARROW_TOWER_IMAGE;
    }

    private BlueArrowTowerImageDrawStrat() {}

    private BlueArrowTowerImageDrawStrat(int tier) {
      this.tier = tier;
    }
  }
}
