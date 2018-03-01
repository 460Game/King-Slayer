package game.view;

import game.message.toClient.NewEntityCommand;
import game.message.toServer.*;
import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntitySpawner;
import game.model.game.model.worldObject.entity.entities.Entities;
import javafx.scene.ImageCursor;
import javafx.scene.paint.Color;
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
  public boolean upgrading = false;
  public boolean deleting = false;
  public boolean selectingBarracks = false;
  private boolean holding = false;

  public KingGameInteractionLayer(ClientGameModel clientGameModel, WorldPanel worldPanel) {
    super(clientGameModel, worldPanel);

    this.model = clientGameModel;
    world = worldPanel;

    world.onGameLeftClick((x, y) -> {
      if (model.clientLoseControl) {
        return;
      }

      if (spawner != null) {
        model.processMessage(new EntityBuildRequest(spawner,
            model.getLocalPlayer().getTeam(), Math.floor(x) + 0.5, Math.floor(y) + 0.5, placingGhost.getHitbox()));
        if (!holding) {
          model.remove(placingGhost);
          spawner = null;
          placingGhost = null;
        }
      } else if (upgrading) {
        model.getEntitiesAt(x.intValue(), y.intValue()).stream().findFirst().ifPresent(entity -> {
          model.processMessage(new UpgradeEntityRequest(entity));
          upgrading = false;
          world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
        });
      } else if (deleting) {
        model.getEntitiesAt(x.intValue(), y.intValue()).stream().findFirst().ifPresent(entity -> {
          model.processMessage(new SellEntityRequest(entity));
          deleting = false;
          world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
        });
      }
    });

    world.onGameRightClick((x, y) -> {
      if (model.clientLoseControl) {
        return;
      }

      holding = false;
      if (spawner != null) {
        model.remove(placingGhost);
        spawner = null;
        placingGhost = null;
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
        if (Util.dist(model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), placingX, placingY) < 4) {
          placingGhost.setX(placingX);
          placingGhost.setY(placingY);
        }
      }

//      world.uiGC.clearRect(0, 0, world.uiGC.getCanvas().getWidth(), world.uiGC.getCanvas().getHeight());
//      if (spawner != null) {
//        world.uiGC.setFill(new Color(1, 1, 1, 0.25));
//        world.uiGC.fillOval(world.gameToScreenX(model.getLocalPlayer().getX()) - toDrawCoords(9.75),
//            world.gameToScreenY(model.getLocalPlayer().getY()) - toDrawCoords(9.75),
//            toDrawCoords(19.5), toDrawCoords(19.5));
//      }
    });

    world.onKeyPress(kc -> {
      if (model.clientLoseControl) {
        return;
      }

      if (placingGhost != null && kc != W && kc != A && kc != S && kc != D && kc != SHIFT) {
        model.removeByID(placingGhost.id);
        placingGhost = null;
        spawner = null;
      }

      if ((upgrading || deleting) && kc != W && kc != A && kc != S && kc != D) {
        upgrading = false;
        deleting = false;
        world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
      }

      if (kc == ESCAPE) {
        clearSelection();
      }

      if (kc == DIGIT1 || kc == NUMPAD1) {
        holding = true;
        this.selectResourceCollector();
      }

      if (kc == DIGIT2 || kc == NUMPAD2) {
        holding = true;
        this.selectWall();
      }

      if (kc == DIGIT3 || kc == NUMPAD3) {
        holding = true;
        this.selectArrowTower();
      }

      if (kc == DIGIT4 || kc == NUMPAD4) {
        holding = true;
        this.selectBarracks();
      }

      if (kc == E) {
        this.selectUpgrade();
      }

      if (kc == Q) {
        this.selectDelete();
      }

      if (kc == SHIFT)
        holding = true;

    });

    world.onKeyRelease(kc -> {
      holding = false;
    });
  }

  public void clearSelection() {
    if (placingGhost != null)
      model.removeByID(placingGhost.id);
    placingGhost = null;
    spawner = null;
    upgrading = false;
    deleting = false;
    world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
  }

  public void selectWall() {
    placingGhost = Entities.makeGhostWall(model.getLocalPlayer().getX(), model.getLocalPlayer().getY());
    spawner = EntitySpawner.WALL_SPAWNER;
    model.processMessage(new NewEntityCommand(placingGhost));
  }

  public void selectResourceCollector() {
    placingGhost = Entities.makeResourceCollectorGhost(model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), model.getLocalPlayer().getTeam());
    spawner = EntitySpawner.RESOURCE_COLLETOR_SPAWNER;
    model.processMessage(new NewEntityCommand(placingGhost));
  }

  public void selectArrowTower() {
    placingGhost = Entities.makeArrowTowerGhost(model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), model.getLocalPlayer().getTeam());
    spawner = EntitySpawner.ARROW_TOWER_SPAWNER;
    model.processMessage(new NewEntityCommand(placingGhost));
  }

  public void selectUpgrade() {
    world.setCursor(new ImageCursor(UPGRADE_CURSOR_IMAGE,
        UPGRADE_CURSOR_IMAGE.getWidth() / 2,
        UPGRADE_CURSOR_IMAGE.getHeight() / 2));
    upgrading = true;
  }

  public void selectDelete() {
    world.setCursor(new ImageCursor(DELETE_CURSOR_IMAGE,
        DELETE_CURSOR_IMAGE.getWidth() / 2,
        DELETE_CURSOR_IMAGE.getHeight() / 2));
    deleting = true;
  }

  public void selectBarracks() {
    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
    spawner = EntitySpawner.BARRACKS_SPAWNER;
    model.processMessage(new NewEntityCommand(placingGhost));

    selectingBarracks = false;
  }

  public void selectRanged() {
    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
    spawner = EntitySpawner.RANGED_BARRACKS_SPAWNER;
    model.processMessage(new NewEntityCommand(placingGhost));

    selectingBarracks = false;
  }

  public void selectSiege() {
    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
    spawner = EntitySpawner.SIEGE_BARRACKS_SPAWNER;
    model.processMessage(new NewEntityCommand(placingGhost));

    selectingBarracks = false;
  }

  public void selectExploration() {
    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
    spawner = EntitySpawner.EXPLORATION_BARRACKS_SPAWNER;
    model.processMessage(new NewEntityCommand(placingGhost));

    selectingBarracks = false;
  }

  public void draw() {
    world.draw();

    world.uiGC.clearRect(0, 0, world.uiGC.getCanvas().getWidth(), world.uiGC.getCanvas().getHeight());
    if (spawner != null) {
      world.uiGC.setFill(new Color(1, 1, 1, 0.25));
      world.uiGC.fillOval(world.gameToScreenX(model.getLocalPlayer().getX()) - toDrawCoords(9.75),
          world.gameToScreenY(model.getLocalPlayer().getY()) - toDrawCoords(9.75),
          toDrawCoords(19.5), toDrawCoords(19.5));
    }
  }
}

