package game.view;

import com.esotericsoftware.minlog.Log;
import game.message.toClient.NewEntityCommand;
import game.message.toServer.EntityBuildRequest;
import game.message.toServer.GoDirectionRequest;
import game.message.toServer.ShootArrowRequest;
import game.message.toServer.StopRequest;
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
import util.Const;
import util.Util;

import java.util.Set;
import java.util.TreeSet;

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
public class GameInteractionLayer extends Region {
    private ClientGameModel model;
    private WorldPanel world;

    private Entity placing;
    private Entity placingGhost;
    private int cost;
    private boolean upgrading = false;
    private boolean selectingBarracks = false;

    public GameInteractionLayer(ClientGameModel clientGameModel) {
        this.model = clientGameModel;
        world = new WorldPanel(clientGameModel);
        world.prefHeightProperty().bind(this.heightProperty());
        world.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(world);

        world.onGameLeftClick((x, y) -> {
            if (model.getLocalPlayer().getRole() == Role.KING && placing != null) {
                if (!placingGhost.getHitbox().getCollidesWith(model, placingGhost.getX(), placingGhost.getY()).skip(1).findAny().isPresent()) {
                    model.processMessage(new EntityBuildRequest(placing,
                            model.getLocalPlayer().getTeam(),
                            TeamResourceData.Resource.WOOD,
                            cost));
                }
                model.remove(placingGhost);
                placing = null;
            } else if (upgrading) {

                Entity entity = model.getEntityAt(x.intValue(), y.intValue());
                System.out.println("clicked at " + x + " " + y + " and hit entity " + entity);
                if (entity != null) {
                    entity.upgrade(); //TODO TODO
                    upgrading = false;
                }
            } else if (model.getLocalPlayer().getRole() == Role.SLAYER) {
                double angle = Math.atan2(y - model.getLocalPlayer().getY(), x - model.getLocalPlayer().getX());
                model.processMessage(new ShootArrowRequest(model.getLocalPlayer().id,
                        model.getLocalPlayer().getX(),
                        model.getLocalPlayer().getY(),
                        angle, model.getLocalPlayer().getTeam()));
            }
        });

        world.onGameRightClick((x, y) -> {
            if (model.getLocalPlayer().getRole() == Role.KING && placing != null) {
                model.remove(placingGhost);
                placing = null;
            } else if (upgrading) {
                upgrading = false;
            }
        });

        world.onGameMouseMove((x, y) -> {
            if (model.getLocalPlayer() != null && model.getLocalPlayer().getRole() == Role.KING && placing != null) {
                double placingX = Math.floor(x) + 0.5;
                double placingY = Math.floor(y) + 0.5;
                if (Util.dist(model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), placingX, placingY) < 5) {
                    placing.setX(placingX);
                    placing.setY(placingY);

                    placingGhost.setX(placingX);
                    placingGhost.setY(placingY);
                }
            }
        });

        int[] dir = {0, 0};

        world.onKeyPress(kc -> {

            if (placingGhost != null && kc != W && kc != A && kc != S && kc != D) {
                model.removeByID(placingGhost.id);
                placingGhost = null;
                placing = null;
            }

            if ((kc == DIGIT1 || kc == NUMPAD1) && model.getLocalPlayer().getRole() == Role.KING) {
                if (!selectingBarracks) {
                    cost = -10;
                    placingGhost = Entities.makeGhostWall(0, 0);
                    placing = Entities.makeBuiltWall(0, 0);
                    model.processMessage(new NewEntityCommand(placingGhost));
                } else {
                    cost = -2;
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
                    placing = Entities.makeMeleeBarracks(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if ((kc == DIGIT2 || kc == NUMPAD2) && model.getLocalPlayer().getRole() == Role.KING) {
                if (!selectingBarracks) {
                    cost = -2;
                    placingGhost = Entities.makeResourceCollectorGhost(0, 0, model.getLocalPlayer().getTeam());
                    placing = Entities.makeResourceCollector(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));
                } else {
                    cost = -2;
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
                    placing = Entities.makeRangedBarracks(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if (kc == DIGIT3 || kc == NUMPAD3) {
                if (!selectingBarracks) {
                    selectingBarracks = true;
                } else {
                    cost = -2;
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
                    placing = Entities.makeSiegeBarracks(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if (kc == DIGIT4 || kc == NUMPAD4) {
                if (!selectingBarracks) {
                    if (model.getLocalPlayer().getRole() == Role.KING) {
                        cost = -20;
                        placingGhost = Entities.makeArrowTowerGhost(0, 0, model.getLocalPlayer().getTeam());
                        placing = Entities.makeArrowTower(0, 0, model.getLocalPlayer().getTeam());
                        model.processMessage(new NewEntityCommand(placingGhost));
                    }
                } else {
                    cost = -2;
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
                    placing = Entities.makeExplorationBarracks(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

        });

        world.onKeyPress(E, () -> {
            if (model.getLocalPlayer().getRole() == Role.KING) {
                upgrading = true;
            }
        });

        world.onKeyPress(TAB, () -> {
            model.processMessage(new StopRequest(model.getLocalPlayer().id));
            int role = (model.getLocalPlayer().getRole().val + 1) % 2;
            for (Entity entity : model.getAllEntities()) {
                if (entity.has(Entity.EntityProperty.TEAM) && entity.has(Entity.EntityProperty.ROLE) && entity.getTeam() == model.getLocalPlayer().getTeam() && entity.getRole().val == role) {
                    model.setLocalPlayer(entity.id);
                    break;
                }
            }
        });

        world.onKeyPress(CAPS, () -> {
            Const.DEBUG_DRAW = !Const.DEBUG_DRAW;
        });

        Runnable sendMovement = () -> {
            if (dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopRequest(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionRequest(model.getLocalPlayer().id, Math.atan2(dir[1], dir[0])));
        };

        world.onKeyPress(() -> {
            dir[1]--;
            sendMovement.run();
        }, W, UP);

        world.onKeyRelease(() -> {
            dir[1]++;
            sendMovement.run();
        }, W, UP);

        world.onKeyRelease(() -> {
            dir[1]--;
            sendMovement.run();
        }, S, DOWN);

        world.onKeyPress(() -> {
            dir[1]++;
            sendMovement.run();
        }, S, DOWN);

        world.onKeyRelease(() -> {
            dir[0]++;
            sendMovement.run();
        }, A, LEFT);
        world.onKeyPress(() -> {
            dir[0]--;
            sendMovement.run();
        }, A, LEFT);

        world.onKeyRelease(() -> {
            dir[0]--;
            sendMovement.run();
        }, D, RIGHT);


        world.onKeyPress(() -> {
            dir[0]++;
            sendMovement.run();
        }, D, RIGHT);

    }

    public void draw() {
        world.draw();
    }
}
