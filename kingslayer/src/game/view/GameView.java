package game.view;

import game.message.GoDirectionMessage;
import game.message.StopMessage;
import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.Team;
import game.model.game.model.worldObject.entity.Entity;
import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import static util.Const.*;
import static util.Util.toDrawCoords;
import static util.Util.toWorldCoords;

public class GameView {

    private ClientGameModel model;

    public GameView(ClientGameModel model) {
        this.model = model;
    }

    public void start(Stage window) {
        Group root = new Group();
        Canvas fgCanvas = new Canvas();
        Canvas bgCanvas = new Canvas();
        Canvas minimapCanvas = new Canvas();
        GraphicsContext fgGC = fgCanvas.getGraphicsContext2D();
        GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();
        GraphicsContext minimapGC = minimapCanvas.getGraphicsContext2D();


        InvalidationListener resize = l -> {
            minimapCanvas.setWidth(Math.min(window.getWidth() / 4, window.getHeight() / 4));
            minimapCanvas.setHeight(Math.min(window.getWidth() / 4, window.getHeight() / 4));
            bgCanvas.setWidth(window.getWidth());
            bgCanvas.setHeight(window.getHeight());
            fgCanvas.setWidth(window.getWidth());
            fgCanvas.setHeight(window.getHeight());
            bgCanvas.setWidth(window.getWidth());
            bgCanvas.setHeight(window.getHeight());
        };

        resize.invalidated(null);

        window.widthProperty().addListener(resize);
        window.heightProperty().addListener(resize);
        root.getChildren().add(bgCanvas);
        root.getChildren().add(fgCanvas);
        root.getChildren().add(minimapCanvas);

        Scene scene = new Scene(root);

        double[] scaleFactor = {1};

        int tick[] = {0};

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                model.update();

                if (model.getLocalPlayer() != null) {

                    minimapGC.setTransform(new Affine(Transform.scale(
                        minimapGC.getCanvas().getWidth() / model.getMapWidth(),
                        minimapGC.getCanvas().getHeight() / model.getMapHeight())));
                    minimapGC.fillRect(0, 0, minimapGC.getCanvas().getWidth(), minimapGC.getCanvas().getHeight());
                    for (int x = 0; x < model.getMapWidth(); x++) {
                        for (int y = 0; y < model.getMapHeight(); y++) {
                            minimapGC.setFill(model.getTile(x, y).getColor());
                            minimapGC.fillRect(x, y, 2, 2);
                        }
                    }
                    //TEMP HACK
                    for (Entity player : model.getAllEntities()) {
                        if (player.team != Team.NEUTRAL) {
                            minimapGC.setFill(player.team.color);
                            minimapGC.fillOval(player.data.x, player.data.y, 3, 3);
                        }
                    }
                    minimapGC.setTransform(new Affine());

                    double x = model.getLocalPlayer().data.x;
                    double y = model.getLocalPlayer().data.y;
                    double gameW = toWorldCoords(window.getWidth() / scaleFactor[0]);
                    double gameH = toWorldCoords(window.getHeight() / scaleFactor[0]);
                    double xt = -toDrawCoords(x * scaleFactor[0]) + window.getWidth() / 2;
                    double yt = -toDrawCoords(y * scaleFactor[0]) + window.getHeight() / 2;
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
                    fgGC.clearRect(0, 0, 11111111, 1111111);

                    model.drawBG(bgGC, GRID_X_SIZE / 2, GRID_Y_SIZE / 2, GRID_X_SIZE, GRID_Y_SIZE, tick[0] > WATER_ANIM_PERIOD / 2);
                    model.drawFG(fgGC, GRID_X_SIZE / 2, GRID_Y_SIZE / 2, GRID_X_SIZE, GRID_Y_SIZE);
                       DropShadow shadow = new DropShadow();
                    shadow.setOffsetY(toDrawCoords(0.2));
                    shadow.setColor(Color.color(0,0,0,.25));
                    shadow.setSpread(0.7);
                   // Reflection reflection = new Reflection();
                   // reflection.setFraction(0.7);

                    fgGC.applyEffect(shadow);
                    tick[0] %= WATER_ANIM_PERIOD;
                    tick[0]++;
                }
            }
        };


        scene.setOnScroll(e -> {
            if (e.getDeltaY() > 0) {
                scaleFactor[0] *= 1.1;
            } else {
                scaleFactor[0] *= 0.9;
            }
        });

        scene.setOnMouseClicked(e -> {

        });

        scene.setOnMouseMoved(e -> {

        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) window.setFullScreen(true);
            if (e.getCode() == KeyCode.W) // Start upward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_UP));
            if (e.getCode() == KeyCode.S) // Start downward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_DOWN));
            if (e.getCode() == KeyCode.A) // Start leftward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_LEFT));
            if (e.getCode() == KeyCode.D) // Start rightward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_RIGHT));
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W) // Stop upward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
            if (e.getCode() == KeyCode.S) // Stop downward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
            if (e.getCode() == KeyCode.A) // Stop leftward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
            if (e.getCode() == KeyCode.D) // Stop rightward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
        });

        animator.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        window.setScene(scene);
    }
}
