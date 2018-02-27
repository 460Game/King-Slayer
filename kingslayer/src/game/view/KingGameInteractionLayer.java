package game.view;

import game.message.toClient.NewEntityCommand;
import game.message.toServer.*;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntitySpawner;
import game.model.game.model.worldObject.entity.entities.Entities;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import util.Const;
import util.Util;

//import static images.Images.DELETE_CURSOR_IMAGE;
//import static images.Images.UPGRADE_CURSOR_IMAGE;
import static images.Images.*;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.DIGIT3;
import static javafx.scene.input.KeyCode.DIGIT4;
import static javafx.scene.input.KeyCode.NUMPAD4;
import static util.Util.toDrawCoords;
import static util.Util.toWorldCoords;

/*
handles interacting with the game
 */
public class KingGameInteractionLayer extends GameInteractionLayer {
  private ClientGameModel model;
  private WorldPanel world;

  //    private Entity placing;
  private EntitySpawner spawner;
  private Entity placingGhost;
  private boolean upgrading = false;
  private boolean deleting = false;
  private boolean selectingBarracks = false;

  public KingGameInteractionLayer(ClientGameModel clientGameModel, WorldPanel worldPanel) {
    super(clientGameModel, worldPanel);

    this.model = clientGameModel;
    world = worldPanel;

    world.onGameLeftClick((x, y) -> {
      if (spawner != null) {
        if (!placingGhost.getHitbox().getCollidesWith(model, placingGhost.getX(), placingGhost.getY()).skip(1).findAny().isPresent()) {
          model.processMessage(new EntityBuildRequest(spawner,
              model.getLocalPlayer().getTeam(), Math.floor(x) + 0.5, Math.floor(y) + 0.5));
          model.remove(placingGhost);
          spawner = null;
          placingGhost = null;
        }
      } else if (upgrading) {
        Entity entity = model.getEntitiesAt(x.intValue(), y.intValue()).stream().findFirst().get();
        System.out.println("clicked at " + x + " " + y + " and hit entity " + entity);
        if (entity != null) {
          model.processMessage(new UpgradeEntityRequest(entity));
          upgrading = false;
          world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
        }
      } else if (deleting) {
        Entity entity = model.getEntitiesAt(x.intValue(), y.intValue()).stream().findFirst().get();
        System.out.println("clicked at " + x + " " + y + " and hit entity " + entity + " to sell");
        if (entity != null) {
          model.processMessage(new SellEntityRequest(entity));
          deleting = false;
          world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
        }
      }
    });

    world.onGameRightClick((x, y) -> {
      if (spawner != null) {
        model.remove(placingGhost);
        spawner = null;
      } else if (upgrading) {
        upgrading = false;
        world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
      } else if (deleting) {
        deleting = false;
        world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
      } else if (selectingBarracks) {
        selectingBarracks = false;
      }
    });

    world.onGameMouseMove((x, y) -> {
      if (model.getLocalPlayer() != null && spawner != null) {
        double placingX = Math.floor(x) + 0.5;
        double placingY = Math.floor(y) + 0.5;
        if (Util.dist(model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), placingX, placingY) < 5) {
          placingGhost.setX(placingX);
          placingGhost.setY(placingY);
        }
      }

//            if (placingGhost != null) {
//                world.uiGC.clearRect(0, 0, world.uiGC.getCanvas().getWidth(), world.uiGC.getCanvas().getHeight());
//                world.uiGC.setFill(new Color(1, 1, 1, 0.25));
////                double placementX =
//                Point2D placement = screenToLocal(toDrawCoords(model.getLocalPlayer().getX()), toDrawCoords(model.getLocalPlayer().getY()));
//                world.uiGC.fillOval(placement.getX(), placement.getY(), toDrawCoords(11), toDrawCoords(11));
//            }
    });

    world.onKeyPress(kc -> {

      if (placingGhost != null && kc != W && kc != A && kc != S && kc != D) {
        model.removeByID(placingGhost.id);
        placingGhost = null;
        spawner = null;
      }

      if ((upgrading || deleting) && kc != W && kc != A && kc != S && kc != D) {
        System.out.println("cancelling");
        upgrading = false;
        deleting = false;
        world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
      }

      if (kc == DIGIT1 || kc == NUMPAD1) {
        if (!selectingBarracks) {
          placingGhost = Entities.makeGhostWall(0, 0);
          spawner = EntitySpawner.WALL_SPAWNER;
          model.processMessage(new NewEntityCommand(placingGhost));
        } else {
          placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
          spawner = EntitySpawner.MELEE_BARRACKS_SPAWNER;
          model.processMessage(new NewEntityCommand(placingGhost));

          selectingBarracks = false;
        }
      }

      if (kc == DIGIT2 || kc == NUMPAD2) {
        if (!selectingBarracks) {
          placingGhost = Entities.makeResourceCollectorGhost(0, 0, model.getLocalPlayer().getTeam());
          spawner = EntitySpawner.RESOURCE_COLLETOR_SPAWNER;
          model.processMessage(new NewEntityCommand(placingGhost));
        } else {
          placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
          spawner = EntitySpawner.RANGED_BARRACKS_SPAWNER;
          model.processMessage(new NewEntityCommand(placingGhost));

          selectingBarracks = false;
        }
      }

      if (kc == DIGIT3 || kc == NUMPAD3) {
        if (!selectingBarracks) {
          selectingBarracks = true;
        } else {
          placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
          spawner = EntitySpawner.SIEGE_BARRACKS_SPAWNER;
          model.processMessage(new NewEntityCommand(placingGhost));

          selectingBarracks = false;
        }
      }

      if (kc == DIGIT4 || kc == NUMPAD4) {
        if (!selectingBarracks) {
          placingGhost = Entities.makeArrowTowerGhost(0, 0, model.getLocalPlayer().getTeam());
          spawner = EntitySpawner.ARROW_TOWER_SPAWNER;
          model.processMessage(new NewEntityCommand(placingGhost));
        } else {
          placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
          spawner = EntitySpawner.EXPLORATION_BARRACKS_SPAWNER;
          model.processMessage(new NewEntityCommand(placingGhost));

          selectingBarracks = false;
        }
      }

      if (kc == E) {
        world.setCursor(new ImageCursor(UPGRADE_CURSOR_IMAGE,
            UPGRADE_CURSOR_IMAGE.getWidth() / 2,
            UPGRADE_CURSOR_IMAGE.getHeight() / 2));
        upgrading = true;
      }

      if (kc == Q) {
        world.setCursor(new ImageCursor(DELETE_CURSOR_IMAGE,
            DELETE_CURSOR_IMAGE.getWidth() / 2,
            DELETE_CURSOR_IMAGE.getHeight() / 2));
        deleting = true;
      }

    });
  }

  public void draw() {
    world.draw();
  }
}

