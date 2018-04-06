package game.view;

import game.message.toClient.NewEntityCommand;
import game.message.toServer.*;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntitySpawner;
import game.model.game.model.worldObject.entity.entities.Entities;
import game.model.game.model.worldObject.entity.entities.Minions;
import game.model.game.model.worldObject.entity.slayer.SlayerData;
import javafx.geometry.Point2D;
import javafx.scene.ImageCursor;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import music.MusicPlayer;
import util.Const;
import util.Util;

//import static images.Images.DELETE_CURSOR_IMAGE;
//import static images.Images.UPGRADE_CURSOR_IMAGE;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.stream.Stream;

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

  private Text danger = new Text("Your king is in danger!");
  private boolean showDanger = false;

  public SlayerGameInteractionLayer(ClientGameModel clientGameModel, WorldPanel worldPanel) {
    super(clientGameModel, worldPanel);

    danger.setFill(Color.WHITE);
    danger.setFont(new Font("Malgun Gothic Bold", 50));
    danger.setStyle("-fx-stroke: black; -fx-stroke-width: 2px;");
    danger.layoutXProperty().bind(this.widthProperty().divide(2).subtract(250));
    danger.layoutYProperty().bind(this.heightProperty().subtract(300));
    danger.setVisible(false);
    this.getChildren().add(danger);

    this.model = clientGameModel;
    world = worldPanel;

    world.onGameLeftClick((x, y) -> {
      if (model.getLoseControl()) {
        return;
      }
      double angle = Math.atan2(y - model.getLocalPlayer().getY(), x - model.getLocalPlayer().getX());

      SlayerData curSlayerData = model.getLocalPlayer().get(Entity.EntityProperty.SLAYER_DATA);
      if (curSlayerData.meleeLastTime <= 0 && curSlayerData.magic >= SlayerData.meleeCost) {
        MusicPlayer.playChargeSound();
      }

      model.processMessage(new SlayerMeleeRequest(model.getLocalPlayer().id,
          model.getLocalPlayer().getX(),
          model.getLocalPlayer().getY(),
          angle, model.getTeam()));
    });

    world.onGameRightClick((x, y) -> {
      if (model.getLoseControl()) {
        return;
      }
      double angle = Math.atan2(y - model.getLocalPlayer().getY(), x - model.getLocalPlayer().getX());

      SlayerData curSlayerData = model.getLocalPlayer().get(Entity.EntityProperty.SLAYER_DATA);
      if (curSlayerData.magic >= SlayerData.arrowCost) {
        MusicPlayer.playArrowSound();
      }

      model.processMessage(new ShootArrowRequest(model.getLocalPlayer().id,
          model.getLocalPlayer().getX(),
          model.getLocalPlayer().getY(),
          angle, model.getTeam()));

    });
  }

  private boolean flag = false;

  public void draw() {
    world.draw();

    Entity opposingSlayer = null;
    Collection<Entity> opposingMinions = Collections.EMPTY_LIST;
    Entity myKing = null;
      for (Entity e: model.getAllEntities()) {
      if (e.has(Entity.EntityProperty.ROLE) && e.has(Entity.EntityProperty.TEAM)) {
        if (e.getRole() == null) {
          System.err.println("e.getRole is null");
        }
        if (e.getTeam() == null) {
          System.err.println("e.getTeam is null");
        }
        if (model.getTeam() == null) {
          System.err.println("model.getTeam is null");
        }
        if (e.getRole() == Role.SLAYER &&
            e.getTeam() != model.getTeam())
          opposingSlayer = e;
        if (e.has(Entity.EntityProperty.MINION_TYPE) &&
            e.get(Entity.EntityProperty.MINION_TYPE) == Minions.MinionType.FIGHTER &&
            e.getTeam() != model.getTeam())
          opposingMinions.add(e);
        if (e.getRole() == Role.KING &&
            e.getTeam() == model.getTeam())
          myKing = e;
      }
    }

    final Entity myFinalKing = myKing;
    if (opposingSlayer != null && myKing != null) {
      final Stream<Boolean> visibleMinions =
          opposingMinions.stream().map(e -> Math.sqrt(Math.pow(myFinalKing.getX() - e.getX(), 2) +
              Math.pow(myFinalKing.getY() - e.getY(), 2)) <= 5);
      boolean inDanger = Math.sqrt(Math.pow(myKing.getX() - opposingSlayer.getX(), 2) +
          Math.pow(myKing.getY() - opposingSlayer.getY(), 2)) <= 5 ||
          visibleMinions.anyMatch((e) -> e);
      if (inDanger && !flag) {
        danger.setVisible(true);
        world.requestFocus();
        flag = true;
        MusicPlayer.playDangerSound();
      } else if (!inDanger && flag) {
        danger.setVisible(false);
        flag = false;
        MusicPlayer.stopDangerSound();
      }
    }
  }
}

