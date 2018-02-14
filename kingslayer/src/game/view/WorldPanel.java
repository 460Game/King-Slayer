package game.view;

import com.esotericsoftware.minlog.Log;
import game.message.toClient.NewEntityCommand;
import game.message.toClient.RemoveEntityCommand;
import game.message.toServer.MakeEntityRequest;
import game.message.toServer.ShootArrowRequest;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.model.game.model.team.TeamResourceData;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.entities.Entities;
import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import static util.Const.*;
import static util.Const.TILE_PIXELS;
import static util.Util.toDrawCoords;
import static util.Util.toWorldCoords;

/**
 * the main node- contains all the world!
 */
public class WorldPanel extends Region {


    WritableImage BGImage;
    private ClientGameModel model;
    private Canvas fgCanvas;
    private Canvas bgCanvas;
    private Canvas uiCanvas;
    private GraphicsContext fgGC;
    private GraphicsContext bgGC;

    private Entity placing;
    private Entity placingGhost;
    private int cost;

    WorldPanel(ClientGameModel model) {
        this.model = model;
        fgCanvas = new Canvas();
        bgCanvas = new Canvas();
        uiCanvas = new Canvas();
        fgGC = fgCanvas.getGraphicsContext2D();
        bgGC = bgCanvas.getGraphicsContext2D();

        this.getChildren().addAll(bgCanvas, fgCanvas, uiCanvas);

        fgCanvas.heightProperty().bind(this.heightProperty());
        fgCanvas.widthProperty().bind(this.widthProperty());
        bgCanvas.heightProperty().bind(this.heightProperty());
        bgCanvas.widthProperty().bind(this.widthProperty());
        uiCanvas.heightProperty().bind(this.heightProperty());
        uiCanvas.widthProperty().bind(this.widthProperty());

        uiCanvas.setFocusTraversable(true);

        uiCanvas.setOnMouseClicked(e -> {
            if (model.getLocalPlayer().role == Role.KING && placing != null) {
                if (placingGhost.data.hitbox.getCollidesWith(model, placingGhost.data.x, placingGhost.data.y).toArray().length <= 1) {
                    model.processMessage(new MakeEntityRequest(placing,
                        model.getLocalPlayer().team,
                        TeamResourceData.Resource.WOOD,
                        cost));
                }
                model.removeByID(placingGhost.id);
                placing = null;
            } else if (model.getLocalPlayer().role == Role.SLAYER) {
//                double angle = Math.atan2(e.getY(), e.getX());

                double xCoords = toWorldCoords(e.getX() - getWidth() / 2);
                double yCoords = toWorldCoords(e.getY() - getHeight() / 2);
                double angle = Math.atan2(yCoords, xCoords);

//                model.processMessage(new ShootArrowRequest(model.getLocalPlayer().id, model.getLocalPlayer().data.x + .56,
//                        model.getLocalPlayer().data.y, angle));
                // 0.56 = arrow radius + player radius
                model.processMessage(new ShootArrowRequest(model.getLocalPlayer().id, model.getLocalPlayer().data.x + 0.56 * Math.cos(angle),
                        model.getLocalPlayer().data.y + 0.56 * Math.sin(angle), angle));

                // TODO problem when player running into own arrow
            }
        });

        uiCanvas.setOnMouseMoved(e -> {
            if (model.getLocalPlayer() != null && model.getLocalPlayer().role == Role.KING && placing != null) {
                //System.out.println("moving to: " + e.getSceneX() + " " + e.getSceneY());
                placing.data.x = Math.floor((toDrawCoords(model.getLocalPlayer().data.x) - uiCanvas.getWidth() / 2 + e.getSceneX()) / TILE_PIXELS) + 0.5;
                placing.data.y = Math.floor((toDrawCoords(model.getLocalPlayer().data.y) - uiCanvas.getHeight() / 2 + e.getSceneY()) / TILE_PIXELS) + 0.5;

                placingGhost.data.x = Math.floor((toDrawCoords(model.getLocalPlayer().data.x) - uiCanvas.getWidth() / 2 + e.getSceneX()) / TILE_PIXELS) + 0.5;
                placingGhost.data.y = Math.floor((toDrawCoords(model.getLocalPlayer().data.y) - uiCanvas.getHeight() / 2 + e.getSceneY()) / TILE_PIXELS) + 0.5;
            }
        });

        uiCanvas.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DIGIT1 || e.getCode() == KeyCode.NUMPAD1) {
                if (model.getLocalPlayer().role == Role.KING && placing == null) {
                    cost = -1000;
                    placingGhost = Entities.makeGhostWall(0, 0);
                    placing = Entities.makeBuiltWall(0, 0);
                    model.processMessage(new NewEntityCommand(placingGhost));
                }
            }

            if (e.getCode() == KeyCode.DIGIT2 || e.getCode() == KeyCode.NUMPAD2) {
                if (model.getLocalPlayer().role == Role.KING && placing == null) {
                    if (model.getLocalPlayer().team == Team.ONE) {
                        cost = -20;
                        placingGhost = Entities.makeResourceCollectorRedGhost(0, 0);
                        placing = Entities.makeResourceCollectorRed(0, 0);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    } else {
                        cost = -20;
                        placingGhost = Entities.makeResourceCollectorBlueGhost(0, 0);
                        placing = Entities.makeResourceCollectorBlue(0, 0);
                        model.processMessage(new NewEntityCommand(placingGhost));
                    }
                }
            }
        });

        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    BGImage = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
                    model.getBG(BGImage,true);
                }
            },
            3000
        );

    }

    double[] scaleFactor = {1};

    int tick[] = {0};

    public void draw() {
        model.update();

        if (model.getLocalPlayer() != null) {

            double x = model.getLocalPlayer().data.x;
            double y = model.getLocalPlayer().data.y;
            double gameW = toWorldCoords(getWidth() / scaleFactor[0]);
            double gameH = toWorldCoords(getHeight() / scaleFactor[0]);
            double xt = -toDrawCoords(x * scaleFactor[0]) + getWidth() / 2;
            double yt = -toDrawCoords(y * scaleFactor[0]) + getHeight() / 2;
            fgGC.setTransform(new Affine());
            bgGC.setTransform(new Affine());
            //       fgGC.transform(new Affine(Affine.translate(CANVAS_WIDTH/2, CANVAS_HEIGHT/2)));
            //                    fgGC.transform(new Affine(Affine.scale(scaleFactor[0], scaleFactor[0])));
            //                    fgGC.transform(new Affine(Affine.scale(scaleFactor[0], scaleFactor[0])));
            //      fgGC.transform(new Affine(Affine.translate(-CANVAS_WIDTH/2, -CANVAS_HEIGHT/2)));
            fgGC.translate(xt, yt);
            bgGC.translate(xt, yt);
            // model.drawFG(fgGC, x, y, gameW, gameH);
            //

            bgGC.setFill(Color.LIGHTCYAN);
            bgGC.fillRect(0, 0, bgCanvas.getWidth(), bgCanvas.getHeight());
            fgGC.clearRect(-1111, -11111, 11111111, 1111111);

            //TODO
            bgGC.drawImage(BGImage, 0, 0);
            model.drawFG(fgGC, GRID_X_SIZE / 2, GRID_Y_SIZE / 2, GRID_X_SIZE, GRID_Y_SIZE);
            DropShadow shadow = new DropShadow();
            shadow.setOffsetY(toDrawCoords(0.2));
            shadow.setColor(Color.color(0, 0, 0, .25));
            shadow.setSpread(0.7);

            fgGC.applyEffect(shadow);
            tick[0] = (tick[0] + 1) % WATER_ANIM_PERIOD;
        }
    }
}
