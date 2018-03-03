package game.view;

import game.message.toClient.NewEntityCommand;
import game.message.toServer.*;
import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntitySpawner;
import game.model.game.model.worldObject.entity.drawStrat.DrawStrat;
import game.model.game.model.worldObject.entity.drawStrat.GhostDrawStrat;
import game.model.game.model.worldObject.entity.drawStrat.ImageDrawStrat;
import game.model.game.model.worldObject.entity.entities.Entities;
import javafx.beans.binding.Bindings;
import javafx.scene.ImageCursor;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
  //protected WorldPanel world;

  //    private Entity placing;
  private EntitySpawner spawner;
  private Entity placingGhost;
  public boolean upgrading = false;
  public boolean deleting = false;
  private boolean holding = false;

  private Text error = new Text("Not enough resources!");
  private boolean showError = false;
  private int errorTime = 0;

  public KingGameInteractionLayer(ClientGameModel clientGameModel, WorldPanel worldPanel) {
    super(clientGameModel, worldPanel);

    this.model = clientGameModel;
    world = worldPanel;

    error.setFill(Color.WHITE);
    error.setFont(new Font("Malgun Gothic Bold", 50));
    error.setStyle("-fx-stroke: black; -fx-stroke-width: 2px;");
    error.layoutXProperty().bind(this.widthProperty().divide(2).subtract(250));
    error.layoutYProperty().bind(this.heightProperty().subtract(300));
    this.getChildren().add(error);

    world.onGameLeftClick((x, y) -> {
      if (model.clientLoseControl) {
        return;
      }

      if (spawner != null && placingGhost != null && !placingGhost.<GhostDrawStrat>get(Entity.EntityProperty.DRAW_STRAT).invalidLocation) {
        model.processMessage(new EntityBuildRequest(spawner,
            model.getLocalPlayer(), Math.floor(x) + 0.5, Math.floor(y) + 0.5, placingGhost.getHitbox()));
        if (!holding) {
          model.remove(placingGhost);
          spawner = null;
          placingGhost = null;
        } else {
          if (model.getResourceData().getResource(spawner.resource) < -spawner.cost) {
            clearSelection();
            showError = true;
          }
        }
      } else if (upgrading) {
        model.getEntitiesAt(x.intValue(), y.intValue()).stream().findFirst().ifPresent(entity -> {
          model.processMessage(new UpgradeEntityRequest(entity));
          if (!holding) {
            upgrading = false;
            world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE,
                GAME_CURSOR_IMAGE.getWidth() / 2,
                GAME_CURSOR_IMAGE.getHeight() / 2));
          }
        });
      } else if (deleting) {
        model.getEntitiesAt(x.intValue(), y.intValue()).stream().findFirst().ifPresent(entity -> {
          model.processMessage(new SellEntityRequest(entity));
          if (!holding) {
            deleting = false;
            world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE,
                GAME_CURSOR_IMAGE.getWidth() / 2,
                GAME_CURSOR_IMAGE.getHeight() / 2));
          }
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
      }
    });

    world.onGameMouseMove((x, y) -> {
      if (model.getLocalPlayer() != null && spawner != null) {
        double placingX = Math.floor(x) + 0.5;
        double placingY = Math.floor(y) + 0.5;
        if (Util.dist(model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), placingX, placingY) <= 4) {
          placingGhost.setX(placingX);
          placingGhost.setY(placingY);
          placingGhost.<GhostDrawStrat>get(Entity.EntityProperty.DRAW_STRAT).invalidLocation = false;
        } else {
          placingGhost.setX(placingX);
          placingGhost.setY(placingY);
          placingGhost.<GhostDrawStrat>get(Entity.EntityProperty.DRAW_STRAT).invalidLocation = true;
        }
      }
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
        holding = true;
        this.selectUpgrade();
      }

      if (kc == Q) {
        holding = true;
        this.selectDelete();
      }

      if (kc == SHIFT)
        holding = true;

      if (kc == KeyCode.TAB) {
        world.requestFocus();
        System.out.println("in interaction");
      }


    });

    world.onKeyRelease(kc -> {
      if (holding && (kc == Q || kc == E)) {
        upgrading = false;
        deleting = false;
        world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE,
            GAME_CURSOR_IMAGE.getWidth() / 2,
            GAME_CURSOR_IMAGE.getHeight() / 2));
      }

      holding = false;
      if (kc == SHIFT && placingGhost != null) {
        model.remove(placingGhost);
        spawner = null;
        placingGhost = null;
      }
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
    if (model.getResourceData().getResource(EntitySpawner.WALL_SPAWNER.resource) >= -EntitySpawner.WALL_SPAWNER.cost) {
      placingGhost = Entities.makeGhostWall(world.screenToGameX(world.mouseX), world.screenToGameY(world.mouseY));
      spawner = EntitySpawner.WALL_SPAWNER;
      model.processMessage(new NewEntityCommand(placingGhost));
    } else {
      showError = true;
    }
  }

  public void selectResourceCollector() {
    if (model.getResourceData().getResource(EntitySpawner.RESOURCE_COLLETOR_SPAWNER.resource) >= -EntitySpawner.RESOURCE_COLLETOR_SPAWNER.cost) {
      placingGhost =
          Entities.makeResourceCollectorGhost(world.screenToGameX(world.mouseX), world.screenToGameY(world.mouseY),
              model.getLocalPlayer().getTeam());
      spawner = EntitySpawner.RESOURCE_COLLETOR_SPAWNER;
      model.processMessage(new NewEntityCommand(placingGhost));
    } else {
      showError = true;
    }
  }

  public void selectArrowTower() {
    if (model.getResourceData().getResource(EntitySpawner.ARROW_TOWER_SPAWNER.resource) >= -EntitySpawner.ARROW_TOWER_SPAWNER.cost) {
      placingGhost = Entities.makeArrowTowerGhost(world.screenToGameX(world.mouseX), world.screenToGameY(world.mouseY),
          model.getLocalPlayer().getTeam());
      spawner = EntitySpawner.ARROW_TOWER_SPAWNER;
      model.processMessage(new NewEntityCommand(placingGhost));
    } else {
      showError = true;
    }
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
    if (model.getResourceData().getResource(EntitySpawner.BARRACKS_SPAWNER.resource) >= -EntitySpawner.BARRACKS_SPAWNER.cost) {
      placingGhost = Entities.makeBarracksGhost(world.screenToGameX(world.mouseX), world.screenToGameY(world.mouseY),
          model.getLocalPlayer().getTeam());
      spawner = EntitySpawner.BARRACKS_SPAWNER;
      model.processMessage(new NewEntityCommand(placingGhost));
    } else {
      showError = true;
    }
  }

  public void draw() {
    world.draw();

    if (showError) {
      error.setVisible(true);
      errorTime++;
      if (errorTime > 10) {
        errorTime = 0;
        showError = false;
      }
    } else {
      error.setVisible(false);
    }

    world.uiGC.clearRect(0, 0, world.uiGC.getCanvas().getWidth(), world.uiGC.getCanvas().getHeight());
    if (spawner != null) {
      world.uiGC.setFill(new Color(1, 1, 1, 0.25));
      world.uiGC.fillOval(world.gameToScreenX(model.getLocalPlayer().getX()) - toDrawCoords(9.75),
          world.gameToScreenY(model.getLocalPlayer().getY()) - toDrawCoords(9.75),
          toDrawCoords(19.5), toDrawCoords(19.5));
    }
  }
}

