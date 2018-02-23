package game.model.game.model.worldObject.entity.drawStrat;

import game.model.game.model.Model;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Velocity;
import images.Images;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static game.model.game.model.worldObject.entity.Entity.EntityProperty.DRAW_DATA;
import static game.model.game.model.worldObject.entity.Entity.EntityProperty.TEAM;
import static game.model.game.model.worldObject.entity.Entity.EntityProperty.VELOCITY;
import static java.lang.Math.decrementExact;
import static util.Util.toDrawCoords;
import static java.lang.Math.PI;

public abstract class DirectionAnimationDrawStrat extends DrawStrat {

//  public AnimationDrawData drawData = AnimationDrawData.makeAnimated();
  public int height = 32;
  public int width = 32;

  public static final DirectionAnimationDrawStrat KING_ANIMATION = new KingDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat SLAYER_ANIMATION = new SlayerDirectionAnimationDrawStrat();

  public static final DirectionAnimationDrawStrat MELEE_ANIMATION = new MeleeMinionDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat RANGED_ANIMATION = new RangedMinionDirectionAnimationDrawStrat();
  public static final DirectionAnimationDrawStrat SIEGE_ANIMATION = new SiegeMinionDirectionAnimationDrawStrat();
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
      AnimationDrawData drawData = entity.<AnimationDrawData>get(DRAW_DATA);
      Point p = imageMap.get(drawData.imageNum + "" + drawData.direction);
      gc.drawImage(this.getImage(entity),
              toDrawCoords(p.x),
              toDrawCoords(p.y),
              width,
              height,
              toDrawCoords(entity.getX() - entity.getHitbox().getWidth() / 2),
              toDrawCoords(entity.getY() - entity.getHitbox().getHeight() / 2),
              toDrawCoords(entity.getHitbox().getWidth()),
              toDrawCoords(entity.getHitbox().getHeight()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void update(Entity entity, Model model) {
    // Update direction of image
    AnimationDrawData drawData = entity.<AnimationDrawData>get(DRAW_DATA);
    double angle = entity.<Velocity>get(VELOCITY).getAngle();
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
    if (entity.<Velocity>get(VELOCITY).getMagnitude() != 0) {
      drawData.count++;
      if (drawData.count > 1) {
        drawData.count = 0;
        drawData.imageNum = (drawData.imageNum + 1) % 3;
      }
    } else {
      drawData.imageNum = 0;
    }
  }

  public double getDrawZ(Entity entity) {
    return entity.getY();
  }

  public static class KingDirectionAnimationDrawStrat extends DirectionAnimationDrawStrat {
    @Override
    Image getImage(Entity entity) {
      switch (entity.<Team>get(TEAM)) {
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
      switch (entity.<Team>get(TEAM)) {
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
      switch (entity.<Team>get(TEAM)) {
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
      switch (entity.<Team>get(TEAM)) {
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
      switch (entity.<Team>get(TEAM)) {
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
      switch (entity.<Team>get(TEAM)) {
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
