package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static util.Util.toDrawCoords;

public abstract class DirectionAnimationDrawStrat extends DrawStrat {

  public static final DirectionAnimationDrawStrat RED_KING_ANIMATION = make(Images.RED_KING_IMAGE_SHEET);
  public static final DirectionAnimationDrawStrat BLUE_KING_ANIMATION = make(Images.BLUE_KING_IMAGE_SHEET);
  public static final DirectionAnimationDrawStrat RED_SLAYER_ANIMATION = make(Images.RED_SLAYER_IMAGE_SHEET);
  public static final DirectionAnimationDrawStrat BLUE_SLAYER_ANIMATION = make(Images.BLUE_SLAYER_IMAGE_SHEET);

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

  private static DirectionAnimationDrawStrat make(Image boxImage) {
    return new DirectionAnimationDrawStrat() {
      @Override
      Image getImage() {
        return boxImage;
      }
    };
  }
}
