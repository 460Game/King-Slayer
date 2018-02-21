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

    private boolean selectingBarracks = false;

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
                        model.getLocalPlayer().data.x,
                        model.getLocalPlayer().data.y,
                        angle, model.getLocalPlayer().team));
                }
            } else if (mb == SECONDARY) {
                if (model.getLocalPlayer().role == Role.KING && placing != null) {
                    model.remove(placingGhost);
                    placing = null;
                } else if (upgrading) {
                    upgrading = false;
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

            if (placingGhost != null && kc != W && kc != A && kc != S && kc != D) {
                model.removeByID(placingGhost.id);
                placingGhost = null;
                placing = null;
            }

            if ((kc == DIGIT1 || kc == NUMPAD1) && model.getLocalPlayer().role == Role.KING) {
                if (!selectingBarracks) {
                    cost = -10;
                    placingGhost = Entities.makeGhostWall(0, 0);
                    placing = Entities.makeBuiltWall(0, 0);
                    model.processMessage(new NewEntityCommand(placingGhost));
                } else {
                    cost = -2;
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().team);
                    placing = Entities.makeMeleeBarracks(0, 0, model.getLocalPlayer().team);
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if ((kc == DIGIT2 || kc == NUMPAD2) && model.getLocalPlayer().role == Role.KING) {
                if (!selectingBarracks) {
                    cost = -2;
                    placingGhost = Entities.makeResourceCollectorGhost(0, 0, model.getLocalPlayer().team);
                    placing = Entities.makeResourceCollector(0, 0, model.getLocalPlayer().team);
                    model.processMessage(new NewEntityCommand(placingGhost));
                } else {
                    cost = -2;
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().team);
                    placing = Entities.makeRangedBarracks(0, 0, model.getLocalPlayer().team);
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if (kc == DIGIT3 || kc == NUMPAD3) {
                if (! selectingBarracks) {
                    selectingBarracks = true;
                } else {
                    cost = -2;
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().team);
                    placing = Entities.makeSiegeBarracks(0, 0, model.getLocalPlayer().team);
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if (kc == DIGIT4 || kc == NUMPAD4) {
                if (!selectingBarracks) {
                    if (model.getLocalPlayer().role == Role.KING) {
                        cost = -20;
                        placingGhost = Entities.makeArrowTowerGhost(0, 0, model.getLocalPlayer().team);
                        placing = Entities.makeArrowTower(0, 0, model.getLocalPlayer().team);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    }
                } else {
                    // TODO make exploration barracks

                    selectingBarracks = false;
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
