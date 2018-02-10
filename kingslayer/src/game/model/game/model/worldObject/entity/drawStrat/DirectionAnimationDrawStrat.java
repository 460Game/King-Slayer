package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static util.Util.toDrawCoords;

public abstract class DirectionAnimationDrawStrat extends DrawStrat {

  public DrawData drawData = DrawData.makeAnimated();
  public int height = 32;
  public int width = 32;

  public static final DirectionAnimationDrawStrat RED_KING_ANIMATION = new RedKingDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat BLUE_KING_ANIMATION = new BlueKingDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat RED_SLAYER_ANIMATION = new RedSlayerDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat BLUE_SLAYER_ANIMATION = new BlueSlayerDirectionAnimationDrawStrat();

  /**
   * Key for figuring out which piece of the character sheet is needed for
   * the king at a given point in time.
   */
  private static Map<String, Point> imageMap = new HashMap<>();

  static {
    // Read in file detailing which images to use at which points in time in the animation
    // of the king.
    Scanner input;
    try {
      input = new Scanner(Images.class.getResource("players.txt").openStream());

      while (input.hasNext()) {
        Point curPoint = new Point(input.nextInt(), input.nextInt());
        if (curPoint.x == -1) break;
        input.nextLine();
        String info = input.nextLine();
        imageMap.put(info, curPoint);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public DrawData initDrawData() {
    return null;
  }

  abstract Image getImage();

  public void draw(Entity entity, GraphicsContext gc) {
    try {
      Point p = imageMap.get(drawData.imageNum + "" + drawData.direction);
      gc.drawImage(getImage(),
          toDrawCoords(entity.data.x),
          toDrawCoords(entity.data.y),
          toDrawCoords(entity.data.hitbox.getWidth()),
          toDrawCoords(entity.data.hitbox.getHeight()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public double getDrawZ(EntityData entity) {
    return entity.y;
  }

  private static class RedKingDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_KING_IMAGE_SHEET;
    }

    public void draw(Entity entity, GraphicsContext gc) {
      try {
        Point p = imageMap.get(drawData.imageNum + "" + drawData.direction);
        gc.drawImage(this.getImage(),
            toDrawCoords(p.x),
            toDrawCoords(p.y),
            width,
            height,
            toDrawCoords(entity.data.x),
            toDrawCoords(entity.data.y),
            toDrawCoords(entity.data.hitbox.getWidth()),
            toDrawCoords(entity.data.hitbox.getHeight()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  private static class BlueKingDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_KING_IMAGE_SHEET;
    }

    public void draw(Entity entity, GraphicsContext gc) {
      try {
        Point p = imageMap.get(drawData.imageNum + "" + drawData.direction);
        gc.drawImage(this.getImage(),
            toDrawCoords(p.x),
            toDrawCoords(p.y),
            width,
            height,
            toDrawCoords(entity.data.x),
            toDrawCoords(entity.data.y),
            toDrawCoords(entity.data.hitbox.getWidth()),
            toDrawCoords(entity.data.hitbox.getHeight()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  private static class RedSlayerDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_SLAYER_IMAGE_SHEET;
    }

    public void draw(Entity entity, GraphicsContext gc) {
      try {
        Point p = imageMap.get(drawData.imageNum + "" + drawData.direction);
        gc.drawImage(this.getImage(),
            toDrawCoords(p.x),
            toDrawCoords(p.y),
            width,
            height,
            toDrawCoords(entity.data.x),
            toDrawCoords(entity.data.y),
            toDrawCoords(entity.data.hitbox.getWidth()),
            toDrawCoords(entity.data.hitbox.getHeight()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  private static class BlueSlayerDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_SLAYER_IMAGE_SHEET;
    }

    public void draw(Entity entity, GraphicsContext gc) {
      try {
        Point p = imageMap.get(drawData.imageNum + "" + drawData.direction);
        gc.drawImage(this.getImage(),
            toDrawCoords(p.x),
            toDrawCoords(p.y),
            width,
            height,
            toDrawCoords(entity.data.x),
            toDrawCoords(entity.data.y),
            toDrawCoords(entity.data.hitbox.getWidth()),
            toDrawCoords(entity.data.hitbox.getHeight()));
      } catch (Exception e) {
        e.printStackTrace();
      }
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
