package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.Model;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntityData;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.scene.paint.Color;

import static util.Util.toDrawCoords;
import static java.lang.Math.PI;

public abstract class DirectionAnimationDrawStrat extends DrawStrat {

  public AnimationDrawData drawData = AnimationDrawData.makeAnimated();
  public int height = 32;
  public int width = 32;

  public static final DirectionAnimationDrawStrat RED_KING_ANIMATION = new RedKingDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat BLUE_KING_ANIMATION = new BlueKingDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat RED_SLAYER_ANIMATION = new RedSlayerDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat BLUE_SLAYER_ANIMATION = new BlueSlayerDirectionAnimationDrawStrat();

  public static final DirectionAnimationDrawStrat RED_RANGED_ANIMATION = new RedRangedMinionDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat BLUE_RANGED_ANIMATION = new BlueRangedMinionDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat RED_MELEE_ANIMATION = new RedMeleeMinionDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat BLUE_MELEE_ANIMATION = new BlueMeleeMinionDirectionAnimationDrawStrat();

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
      gc.drawImage(this.getImage(),
              toDrawCoords(p.x),
              toDrawCoords(p.y),
              width,
              height,
              toDrawCoords(entity.data.x - entity.data.hitbox.getWidth() / 2),
              toDrawCoords(entity.data.y - entity.data.hitbox.getHeight() / 2),
              toDrawCoords(entity.data.hitbox.getWidth()),
              toDrawCoords(entity.data.hitbox.getHeight()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Entity entity, Model model) {
    // Update direction of image
    double angle = entity.data.updateData.velocity.getAngle();
    if (angle >= -0.75 * PI && angle < -0.25 * PI) {
      drawData.direction = 'N';
    } else if (angle >= -0.25 * PI && angle < 0.25 * PI) {
      drawData.direction = 'E';
    } else if (angle >= 0.25 * PI && angle < 0.75 * PI) {
      drawData.direction = 'S';
    } else if (angle >= 0.75 * PI || angle < -0.75 * PI) {
      drawData.direction = 'W';
    }

    // Update image being used
    if (entity.data.updateData.velocity.getMagnitude() != 0) {
      drawData.count++;
      if (drawData.count > 11) {
        drawData.count = 0;
        drawData.imageNum = (drawData.imageNum + 1) % 3;
      }
    } else {
      drawData.imageNum = 0;
    }
  }

  public double getDrawZ(EntityData entity) {
    return entity.y;
  }

  public static class RedKingDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_KING_IMAGE_SHEET;
    }
  }
  public static class BlueKingDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_KING_IMAGE_SHEET;
    }
  }
  public static class RedSlayerDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_SLAYER_IMAGE_SHEET;
    }
  }
  public static class BlueSlayerDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_SLAYER_IMAGE_SHEET;
    }
  }

  public static class RedRangedMinionDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_SLAYER_IMAGE_SHEET;
    }
  }
  public static class BlueRangedMinionDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_RANGED_IMAGE_SHEET;
    }
  }
  public static class RedMeleeMinionDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.RED_SLAYER_IMAGE_SHEET;
    }
  }
  public static class BlueMeleeMinionDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage() {
      return Images.BLUE_SLAYER_IMAGE_SHEET;
    }
  }

  private DirectionAnimationDrawStrat(){}
}
