package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static util.Util.toDrawCoords;

public abstract class DirectionAnimationDrawStrat extends DrawStrat {

  public static final DirectionAnimationDrawStrat RED_KING_ANIMATION = new RedKingDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat BLUE_KING_ANIMATION = new BlueKingDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat RED_SLAYER_ANIMATION = new RedSlayerDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat BLUE_SLAYER_ANIMATION = new BlueSlayerDirectionAnimationDrawStrat();

  @Override
  public DrawData initDrawData() {
    return null;
  }

  abstract Image getImage();

  public void draw(Entity entity, GraphicsContext gc) {
    gc.drawImage(getImage(), toDrawCoords(entity.data.x),
        toDrawCoords(entity.data.y),
        toDrawCoords(entity.data.hitbox.getWidth()),
        toDrawCoords(entity.data.hitbox.getHeight()));
  }

  public double getDrawZ(EntityData entity) {
    return entity.y;
  }

  private static class RedKingDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BOX_IMAGE;
    }
  }
  private static class BlueKingDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BOX_IMAGE;
    }
  }
  private static class RedSlayerDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BOX_IMAGE;
    }
  }
  private static class BlueSlayerDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BOX_IMAGE;
    }
  }

  private static DirectionAnimationDrawStrat make(Image boxImage) {
    return new DirectionAnimationDrawStrat() {
      @Override
      Image getImage() {
        return boxImage;
      }
    };
  }

  private DirectionAnimationDrawStrat(){}
}
