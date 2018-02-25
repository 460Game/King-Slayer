package game.view;

import game.message.toClient.NewEntityCommand;
import game.message.toServer.*;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.EntitySpawner;
import game.model.game.model.worldObject.entity.entities.Entities;
import javafx.scene.ImageCursor;
import javafx.scene.layout.Region;
import util.Const;
import util.Util;

//import static images.Images.DELETE_CURSOR_IMAGE;
//import static images.Images.UPGRADE_CURSOR_IMAGE;
import static images.Images.*;
import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.DIGIT3;
import static javafx.scene.input.KeyCode.DIGIT4;
import static javafx.scene.input.KeyCode.NUMPAD4;
import static util.Util.toWorldCoords;

/*
handles interacting with the game
 */
public class GameInteractionLayer extends Region {
    private ClientGameModel model;
    private WorldPanel world;

//    private Entity placing;
    private EntitySpawner spawner;
    private Entity placingGhost;
    private boolean upgrading = false;
    private boolean deleting = false;
    private boolean selectingBarracks = false;

    public GameInteractionLayer(ClientGameModel clientGameModel, WorldPanel worldPanel) {
        this.model = clientGameModel;
        world = worldPanel;
        world.prefHeightProperty().bind(this.heightProperty());
        world.prefWidthProperty().bind(this.widthProperty());
        this.getChildren().add(world);

        world.onGameLeftClick((x, y) -> {
            if (model.getLocalPlayer().getRole() == Role.KING && spawner != null) {
                if (!placingGhost.getHitbox().getCollidesWith(model, placingGhost.getX(), placingGhost.getY()).skip(1).findAny().isPresent()) {
                    model.processMessage(new EntityBuildRequest(spawner,
                            model.getLocalPlayer().getTeam()));
                    model.remove(placingGhost);
                    spawner = null;
                }
            } else if (upgrading) {
                Entity entity = model.getEntityAt(x.intValue(), y.intValue());
                System.out.println("clicked at " + x + " " + y + " and hit entity " + entity);
                if (entity != null) {
                    model.processMessage(new UpgradeEntityRequest(entity));
                    upgrading = false;
                    world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
                }
            } else if (deleting) {
                Entity entity = model.getEntityAt(x.intValue(), y.intValue());
                System.out.println("clicked at " + x + " " + y + " and hit entity " + entity + " to sell");
                if (entity != null) {
                    model.processMessage(new SellEntityRequest(entity));
                    deleting = false;
                    world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
                }
            } else if (model.getLocalPlayer().getRole() == Role.SLAYER) {
                double angle = Math.atan2(y - model.getLocalPlayer().getY(), x - model.getLocalPlayer().getX());
                model.processMessage(new SlayerMeleeRequest(model.getLocalPlayer().id,
                        model.getLocalPlayer().getX(),
                        model.getLocalPlayer().getY(),
                        angle, model.getLocalPlayer().getTeam()));
            }
        });

        world.onGameRightClick((x, y) -> {
            if (model.getLocalPlayer().getRole() == Role.KING && spawner != null) {
                model.remove(placingGhost);
                spawner = null;
            } else if (upgrading) {
                upgrading = false;
            } else if (deleting) {
                deleting = false;
            } else if (selectingBarracks) {
                selectingBarracks = false;
            } else if (model.getLocalPlayer().getRole() == Role.SLAYER) {
                double angle = Math.atan2(y - model.getLocalPlayer().getY(), x - model.getLocalPlayer().getX());
                model.processMessage(new ShootArrowRequest(model.getLocalPlayer().id,
                        model.getLocalPlayer().getX(),
                        model.getLocalPlayer().getY(),
                        angle, model.getLocalPlayer().getTeam()));
            }
        });

        world.onGameMouseMove((x, y) -> {
            if (model.getLocalPlayer() != null && model.getLocalPlayer().getRole() == Role.KING && spawner != null) {
                double placingX = Math.floor(x) + 0.5;
                double placingY = Math.floor(y) + 0.5;
                if (Util.dist(model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), placingX, placingY) < 5) {
                    spawner.x = placingX;
                    spawner.y = placingY;

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
                spawner = null;
            }

            if ((upgrading || deleting) && kc != W && kc != A && kc != S && kc != D) {
                System.out.println("cancelling");
                upgrading = false;
                deleting = false;
                world.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
            }

            if ((kc == DIGIT1 || kc == NUMPAD1) && model.getLocalPlayer().getRole() == Role.KING) {
                if (!selectingBarracks) {
                    placingGhost = Entities.makeGhostWall(0, 0);
                    spawner = new EntitySpawner((point, team) -> Entities.makeBuiltWall(point.getKey(), point.getValue(), team),
                        TeamResourceData.Resource.WOOD, -10, 0, 0);
//                    placing = Entities.makeBuiltWall(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));
                } else {
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
                    spawner = new EntitySpawner((point, team) -> Entities.makeMeleeBarracks(point.getKey(), point.getValue(), team),
                        TeamResourceData.Resource.WOOD, -10, 0, 0);
//                    placing = Entities.makeMeleeBarracks(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if ((kc == DIGIT2 || kc == NUMPAD2) && model.getLocalPlayer().getRole() == Role.KING) {
                if (!selectingBarracks) {
                    placingGhost = Entities.makeResourceCollectorGhost(0, 0, model.getLocalPlayer().getTeam());
                    spawner = new EntitySpawner((point, team) -> Entities.makeResourceCollector(point.getKey(), point.getValue(), team),
                        TeamResourceData.Resource.WOOD, -10, 0, 0);
//                    placing = Entities.makeResourceCollector(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));
                } else {
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
                    spawner = new EntitySpawner((point, team) -> Entities.makeRangedBarracks(point.getKey(), point.getValue(), team),
                        TeamResourceData.Resource.WOOD, -10, 0, 0);
//                    placing = Entities.makeRangedBarracks(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if (kc == DIGIT3 || kc == NUMPAD3) {
                if (!selectingBarracks) {
                    selectingBarracks = true;
                } else {
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
                    spawner = new EntitySpawner((point, team) -> Entities.makeSiegeBarracks(point.getKey(), point.getValue(), team),
                        TeamResourceData.Resource.WOOD, -10, 0, 0);
//                    placing = Entities.makeSiegeBarracks(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if (kc == DIGIT4 || kc == NUMPAD4) {
                if (!selectingBarracks) {
                    if (model.getLocalPlayer().getRole() == Role.KING) {
                        placingGhost = Entities.makeArrowTowerGhost(0, 0, model.getLocalPlayer().getTeam());
                        spawner = new EntitySpawner((point, team) -> Entities.makeArrowTower(point.getKey(), point.getValue(), team),
                            TeamResourceData.Resource.WOOD, -20, 0, 0);
//                        placing = Entities.makeArrowTower(0, 0, model.getLocalPlayer().getTeam());
                        model.processMessage(new NewEntityCommand(placingGhost));
                    }
                } else {
                    placingGhost = Entities.makeBarracksGhost(0, 0, model.getLocalPlayer().getTeam());
                    spawner = new EntitySpawner((point, team) -> Entities.makeExplorationBarracks(point.getKey(), point.getValue(), team),
                        TeamResourceData.Resource.WOOD, -10, 0, 0);
//                    placing = Entities.makeExplorationBarracks(0, 0, model.getLocalPlayer().getTeam());
                    model.processMessage(new NewEntityCommand(placingGhost));

                    selectingBarracks = false;
                }
            }

            if (kc == E) {
                if (model.getLocalPlayer().getRole() == Role.KING) {
                    world.setCursor(new ImageCursor(UPGRADE_CURSOR_IMAGE, UPGRADE_CURSOR_IMAGE.getWidth() / 2, UPGRADE_CURSOR_IMAGE.getHeight() / 2));
                    upgrading = true;
                }
            }

            if (kc == Q) {
                if (model.getLocalPlayer().getRole() == Role.KING) {
                    world.setCursor(new ImageCursor(DELETE_CURSOR_IMAGE, DELETE_CURSOR_IMAGE.getWidth() / 2, DELETE_CURSOR_IMAGE.getHeight() / 2));
                    deleting = true;
                }
            }

        });

//        world.onKeyPress(E, () -> {
//            System.out.println("pressed E");
//            if (model.getLocalPlayer().getRole() == Role.KING) {
//                System.out.println("are king");
//                world.setCursor(new ImageCursor(UPGRADE_CURSOR_IMAGE, UPGRADE_CURSOR_IMAGE.getWidth() / 2, UPGRADE_CURSOR_IMAGE.getHeight() / 2));
//                upgrading = true;
//            }
//        });

//        world.onKeyPress(Q, () -> {
//            System.out.println("pressed Q");
//            if (model.getLocalPlayer().getRole() == Role.KING) {
//                world.setCursor(new ImageCursor(DELETE_CURSOR_IMAGE, DELETE_CURSOR_IMAGE.getWidth() / 2, DELETE_CURSOR_IMAGE.getHeight() / 2));
//                deleting = true;
//            }
//            System.out.println("did it work? " + deleting);
//        });

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
