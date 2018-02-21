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

import static java.lang.Math.decrementExact;
import static util.Util.toDrawCoords;
import static java.lang.Math.PI;

public abstract class DirectionAnimationDrawStrat extends DrawStrat {

  public AnimationDrawData drawData = AnimationDrawData.makeAnimated();
  public int height = 32;
  public int width = 32;

  public static final DirectionAnimationDrawStrat KING_ANIMATION = new KingDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat SLAYER_ANIMATION = new SlayerDirectionAnimationDrawStrat();

  public static final DirectionAnimationDrawStrat RANGED_ANIMATION = new RangedMinionDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat MELEE_ANIMATION = new MeleeMinionDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat RESOURCE_MINION_ANIMATION = new ResourceMinionDirectionAnimationDrawStrat();

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

  abstract Image getImage(Entity entity);

  public void draw(Entity entity, GraphicsContext gc) {
    try {
      Point p = imageMap.get(drawData.imageNum + "" + drawData.direction);
      gc.drawImage(this.getImage(entity),
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

  public static class KingDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_KING_IMAGE_SHEET;
        case TWO:
          return Images.BLUE_KING_IMAGE_SHEET;
        default:
          return null;
      }
    }
  }

  public static class SlayerDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_SLAYER_IMAGE_SHEET;
        case TWO:
          return Images.BLUE_SLAYER_IMAGE_SHEET;
        default:
          return null;
      }
    }
  }

  public static class RangedMinionDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_RANGED_IMAGE_SHEET;
        case TWO:
          return Images.BLUE_RANGED_IMAGE_SHEET;
        default:
          return null;
      }
    }
  }

  public static class MeleeMinionDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_MELEE_IMAGE_SHEET;
        case TWO:
          return Images.BLUE_MELEE_IMAGE_SHEET;
        default:
          return null;
      }
    } // TODO
  }

  public static class SiegeMinionDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_SIEGE_IMAGE_SHEET;
        case TWO:
          return Images.BLUE_SIEGE_IMAGE_SHEET;
        default:
          return null;
      }

    }
  }

  public static class ResourceMinionDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage(Entity entity) {
      switch (entity.team) {
        case ONE:
          return Images.RED_RESOURCE_MINION_IMAGE_SHEET;
        case TWO:
          return Images.BLUE_RESOURCE_MIONION_IMAGE_SHEET;
        default:
          return null;
      }

    }
  }

  private DirectionAnimationDrawStrat(){}
}
