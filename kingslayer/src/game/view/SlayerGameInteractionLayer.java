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
public class SlayerGameInteractionLayer extends GameInteractionLayer {
  private ClientGameModel model;
  private WorldPanel world;

  public SlayerGameInteractionLayer(ClientGameModel clientGameModel, WorldPanel worldPanel) {
    super(clientGameModel, worldPanel);

    this.model = clientGameModel;
    world = worldPanel;

    world.onGameLeftClick((x, y) -> {
      if (model.clientLoseControl) {
        return;
      }
      double angle = Math.atan2(y - model.getLocalPlayer().getY(), x - model.getLocalPlayer().getX());
      model.processMessage(new SlayerMeleeRequest(model.getLocalPlayer().id,
          model.getLocalPlayer().getX(),
          model.getLocalPlayer().getY(),
          angle, model.getLocalPlayer().getTeam()));

    });

    world.onGameRightClick((x, y) -> {
      if (model.clientLoseControl) {
        return;
      }
      double angle = Math.atan2(y - model.getLocalPlayer().getY(), x - model.getLocalPlayer().getX());
      model.processMessage(new ShootArrowRequest(model.getLocalPlayer().id,
          model.getLocalPlayer().getX(),
          model.getLocalPlayer().getY(),
          angle, model.getLocalPlayer().getTeam()));

    });
  }

  public void draw() {
    world.draw();
  }
}

