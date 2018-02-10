package game.view;

import game.message.GoDirectionMessage;
import game.message.StopMessage;
import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.Team;
import game.model.game.model.worldObject.entity.Entity;
import game.model.game.model.worldObject.entity.collideStrat.CollisionStrat;
import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
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
        Canvas fgCanvas = new Canvas(INIT_SCREEN_WIDTH, INIT_SCREEN_HEIGHT);
        Canvas bgCanvas = new Canvas(INIT_SCREEN_WIDTH, INIT_SCREEN_HEIGHT);
        Canvas minimapCanvas = new Canvas(INIT_SCREEN_WIDTH / 3, INIT_SCREEN_HEIGHT / 3);
        GraphicsContext gc = fgCanvas.getGraphicsContext2D();
        GraphicsContext minimapGC = minimapCanvas.getGraphicsContext2D();


        InvalidationListener resize = l -> {
            minimapCanvas.setWidth(Math.min(window.getWidth() / 3, window.getHeight() / 3));
            minimapCanvas.setHeight(Math.min(window.getWidth() / 3, window.getHeight() / 3));
            bgCanvas.setWidth(window.getWidth());
            bgCanvas.setHeight(window.getHeight());
            fgCanvas.setWidth(window.getWidth());
            fgCanvas.setHeight(window.getHeight());
        };

        window.widthProperty().addListener(resize);
        window.heightProperty().addListener(resize);

        root.getChildren().add(fgCanvas);
        root.getChildren().add(bgCanvas);
        root.getChildren().add(minimapCanvas);

        Scene scene = new Scene(root);

        double[] scaleFactor = {1};

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
                     for(Entity player : model.getAllEntities()) {
                         if(player.team != Team.NEUTRAL) {
                             minimapGC.setFill(player.team.color);
                              minimapGC.fillOval(player.data.x,player.data.y,3,3);
                           }
                       }
                    minimapGC.setTransform(new Affine());

                    double x = model.getLocalPlayer().data.x;
                    double y = model.getLocalPlayer().data.y;
                    double gameW = toWorldCoords(window.getWidth()/scaleFactor[0]);
                    double gameH = toWorldCoords(window.getHeight()/scaleFactor[0]);
                    double xt = -toDrawCoords(x*scaleFactor[0]) + window.getWidth() / 2;
                    double yt = -toDrawCoords(y*scaleFactor[0]) + window.getHeight() / 2;
                    gc.setTransform(new Affine());
             //       fgGC.transform(new Affine(Affine.translate(CANVAS_WIDTH/2, CANVAS_HEIGHT/2)));
                    gc.transform(new Affine(Affine.scale(scaleFactor[0], scaleFactor[0])));
              //      fgGC.transform(new Affine(Affine.translate(-CANVAS_WIDTH/2, -CANVAS_HEIGHT/2)));
                    gc.translate(xt, yt);
                   // model.draw(fgGC, x, y, gameW, gameH);
                    //

                    gc.setFill(Color.LIGHTCYAN);
                    gc.fillRect(-100000, -100000, 100000000, 10000000);
                    model.draw(gc, GRID_X_SIZE/2, GRID_Y_SIZE/2, GRID_X_SIZE, GRID_Y_SIZE, false);
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

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) window.setFullScreen(true);
            if (e.getCode() == KeyCode.W) // Start upward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_UP));
            if (e.getCode() == KeyCode.S) // Start downward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_DOWN));
            if (e.getCode() == KeyCode.A) // Start leftward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_LEFT));
            if ( e.getCode() == KeyCode.D) // Start rightward movement.
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

        window.setScene(scene);
        animator.start();
    }
}
