package game.view;

import game.message.toClient.NewEntityCommand;
import game.message.toServer.EntityBuildRequest;
import game.message.toServer.ShootArrowRequest;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Entities;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.DIGIT3;
import static javafx.scene.input.KeyCode.DIGIT4;
import static javafx.scene.input.KeyCode.NUMPAD4;
import static javafx.scene.input.MouseButton.PRIMARY;
import static javafx.scene.input.MouseButton.SECONDARY;
import static util.Const.TILE_PIXELS;
import static util.Util.toDrawCoords;
import static util.Util.toWorldCoords;

/*
handles interacting with the game
 */
public class GameInteractionLayer extends Region  {
    private Canvas uiCanvas;
    private ClientGameModel model;

    private Entity placing;
    private Entity placingGhost;
    private int cost;

    private boolean upgrading = false;

    public GameInteractionLayer(ClientGameModel clientGameModel) {
        this.model = clientGameModel;
        uiCanvas = new Canvas();
        uiCanvas.heightProperty().bind(this.heightProperty());
        uiCanvas.widthProperty().bind(this.widthProperty());

        this.getChildren().addAll(uiCanvas);

        uiCanvas.setFocusTraversable(true);

        uiCanvas.setOnMouseClicked(e -> {
            MouseButton mb = e.getButton();

            if (mb == PRIMARY) {
                if (model.getLocalPlayer().role == Role.KING && placing != null) {
                    if (!placingGhost.data.hitbox.getCollidesWith(model, placingGhost.data.x, placingGhost.data.y).skip(1).findAny().isPresent()) {
                        model.processMessage(new EntityBuildRequest(placing,
                            model.getLocalPlayer().team,
                            TeamResourceData.Resource.WOOD,
                            cost));
                    }
                    model.remove(placingGhost);
                    placing = null;
                } else if (upgrading) {
                  int x = (int) (toDrawCoords(model.getLocalPlayer().data.x) - uiCanvas.getWidth() / 2 + e.getSceneX()) / TILE_PIXELS;
                  int y = (int) (toDrawCoords(model.getLocalPlayer().data.y) - uiCanvas.getHeight() / 2 + e.getSceneY()) / TILE_PIXELS;
                  Entity entity = model.getEntityAt(x, y);
                  System.out.println("clicked at " + x + " " + y + " and hit entity " + entity);
                  if (entity != null) {
                    entity.upgrade(model);
                    upgrading = false;
                  }
                } else if (model.getLocalPlayer().role == Role.SLAYER) {

                    double xCoords = toWorldCoords(e.getX() - getWidth() / 2);
                    double yCoords = toWorldCoords(e.getY() - getHeight() / 2);
                    double angle = Math.atan2(yCoords, xCoords);
                    model.processMessage(new ShootArrowRequest(model.getLocalPlayer().id,
                        model.getLocalPlayer().data.x + 0.56 * Math.cos(angle),
                        model.getLocalPlayer().data.y + 0.56 * Math.sin(angle),
                        angle));

                    // TODO problem when player running into own arrow
                }
            } else if (mb == SECONDARY) {
                if (model.getLocalPlayer().role == Role.KING && placing != null) {
                    model.remove(placingGhost);
                    placing = null;
                }
            }
        });

        uiCanvas.setOnMouseMoved(e -> {
            if (model.getLocalPlayer() != null && model.getLocalPlayer().role == Role.KING && placing != null) {
                double placingX = Math.floor((toDrawCoords(model.getLocalPlayer().data.x) - uiCanvas.getWidth() / 2 + e.getSceneX()) / TILE_PIXELS) + 0.5;
                double placingY = Math.floor((toDrawCoords(model.getLocalPlayer().data.y) - uiCanvas.getHeight() / 2 + e.getSceneY()) / TILE_PIXELS) + 0.5;
                if (Math.sqrt(Math.pow(model.getLocalPlayer().data.x - placingX, 2) + Math.pow(model.getLocalPlayer().data.y - placingY, 2)) < 5) {
                    placing.data.x = placingX;
                    placing.data.y = placingY;

                    placingGhost.data.x = placingX;
                    placingGhost.data.y = placingY;
                }
            }
        });

        uiCanvas.setOnKeyPressed(e -> {
            KeyCode kc = e.getCode();

            if (placingGhost != null) {
                model.removeByID(placingGhost.id);
                placingGhost = null;
                placing = null;
            }

            if (kc == DIGIT1 || kc == NUMPAD1) {
                if (model.getLocalPlayer().role == Role.KING) {
                    cost = -10;
                    placingGhost = Entities.makeGhostWall(0, 0);
                    placing = Entities.makeBuiltWall(0, 0);
                    model.processMessage(new NewEntityCommand(placingGhost));
                }
            }

            if (kc == DIGIT2 || kc == NUMPAD2) {
                if (model.getLocalPlayer().role == Role.KING) {
                    if (model.getLocalPlayer().team == Team.ONE) {
                        cost = -2;
                        placingGhost = Entities.makeResourceCollectorRedGhost(0, 0);
                        placing = Entities.makeResourceCollectorRed(0, 0);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    } else {
                        cost = -2;
                        placingGhost = Entities.makeResourceCollectorBlueGhost(0, 0);
                        placing = Entities.makeResourceCollectorBlue(0, 0);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    }
                }
            }

            if (kc == DIGIT3 || kc == NUMPAD3) {
                if (model.getLocalPlayer().role == Role.KING) {
                    if (model.getLocalPlayer().team == Team.ONE) {
                        cost = -2;
                        placingGhost = Entities.makeRedBarracksGhost(0, 0);
                        placing = Entities.makeRedBarracks(0, 0);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    } else {
                        // TODO
                        cost = -2;
                        placingGhost = Entities.makeBlueBarracksGhost(0, 0);
                        placing = Entities.makeBlueBarracks(0, 0);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    }
                }
            }

            if (kc == DIGIT4 || kc == NUMPAD4) {
                if (model.getLocalPlayer().role == Role.KING) {
                    if (model.getLocalPlayer().team == Team.ONE) {
                        cost = -20;
                        placingGhost = Entities.makeRedArrowTower(0, 0);
                        placing = Entities.makeRedArrowTower(0, 0);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    } else {
                        cost = -20;
                        placingGhost = Entities.makeBlueArrowTower(0, 0);
                        placing = Entities.makeBlueArrowTower(0, 0);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    }
                }
            }

          if (e.getCode() == KeyCode.E) {
            if (model.getLocalPlayer().role == Role.KING) {
              upgrading = true;
            }
          }

        });
    }
}
