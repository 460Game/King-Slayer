package game.view;

import static Util.Const.*;


import game.message.playerMoveMessage.*;
import game.model.Game.Model.ClientGameModel;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

public class ClientView {

    private ClientGameModel model;

    private AnimationTimer animator;

    private Stage window;

    public ClientView(ClientGameModel model) {
        this.model = model;
    }

    public void start(Stage window) {
        this.window = window;

        window.setResizable(true);
        window.setTitle("King Slayer");

        Group root = new Group();
        Canvas canvas = new Canvas(INIT_SCREEN_WIDTH, INIT_SCREEN_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        window.widthProperty().addListener(l -> {
            canvas.setWidth(window.getWidth());
        });

        window.heightProperty().addListener(l -> {
            canvas.setHeight(window.getHeight());
        });

        window.setFullScreen(true);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root);

        double[] scaleFactor = {1.0};

        model.start();

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                double gameW = scaleFactor[0] * window.getWidth() / TILE_PIXELS;
                double gameH = scaleFactor[0] * window.getHeight() / TILE_PIXELS;

                if(model.getLocalPlayer() != null) {
                    gc.transform(new Affine(Affine.translate(-model.getLocalPlayer().getX() * TILE_PIXELS + window.getWidth() / 2, -model.getLocalPlayer().getY() * TILE_PIXELS + window.getHeight() / 2)));
                    model.draw(gc, model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), gameW, gameH);
                    gc.transform(new Affine(Affine.translate(model.getLocalPlayer().getX() * TILE_PIXELS - window.getWidth() / 2, model.getLocalPlayer().getY() * TILE_PIXELS - window.getHeight() / 2)));
                }
            }
        };

        scene.setOnScroll(e -> {
            if (e.getDeltaY() < 0) {
                scaleFactor[0] *= 0.9;
                gc.transform(new Affine(Affine.scale(0.9, 0.9)));
            } else {
                scaleFactor[0] *= 1.1;
                gc.transform(new Affine(Affine.scale(1.1, 1.1)));
            }
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                window.setFullScreen(true);
            }
            if (e.getCode() == KeyCode.S) {
                     gc.transform(new Affine(Affine.translate(0, -200)));
              //  model.processMessage(new PlayerUp(0));
            }
            //
            if (e.getCode() == KeyCode.W) {

                      gc.transform(new Affine(Affine.translate(0, 200)));
               // model.processMessage(new PlayerDown(0));
            }
            //
            if (e.getCode() == KeyCode.D) {

                    gc.transform(new Affine(Affine.translate(-200, 0)));
               // model.processMessage(new PlayerLeft(0));
            }
            //
            if (e.getCode() == KeyCode.A) {

                    gc.transform(new Affine(Affine.translate(200, 0)));
              //  model.processMessage(new PlayerRight(0));
            }
            //
            //           model.playerA.right();
            if (e.getCode() == KeyCode.UP) {

             //   model.processMessage(new PlayerUp(0));
                model.processMessage(new PlayerUp(model.getLocalPlayer().getId()));
            }
            if (e.getCode() == KeyCode.DOWN) {

             //   model.processMessage(new PlayerDown(0));
                model.processMessage(new PlayerDown(model.getLocalPlayer().getId()));
            }
            if (e.getCode() == KeyCode.LEFT) {

              //  model.processMessage(new PlayerLeft(0));
                model.processMessage(new PlayerLeft(model.getLocalPlayer().getId()));
            }
            if (e.getCode() == KeyCode.RIGHT){

              //  model.processMessage(new PlayerRight(0));
                model.processMessage(new PlayerRight(model.getLocalPlayer().getId()));
            }
        });

        scene.setOnKeyReleased(e -> {
          //  if (e.getCode() == KeyCode.W)
               // model.processMessage(new PlayerStopVert(0));
            //     model.playerA.stopVert();
           // if (e.getCode() == KeyCode.S)
              //  model.processMessage(new PlayerStopVert(0));
            //        model.playerA.stopVert();
           // if (e.getCode() == KeyCode.A)
              //  model.processMessage(new PlayerStopHorz(0));
            //         model.playerA.stopHorz();
           // if (e.getCode() == KeyCode.D)
             //   model.processMessage(new PlayerStopHorz(0));
            //         model.playerA.stopHorz();
            if (e.getCode() == KeyCode.RIGHT) {
            //    model.processMessage(new PlayerStopHorz(0));
                model.processMessage(new PlayerStopHorz(model.getLocalPlayer().getId()));
            }
            if (e.getCode() == KeyCode.LEFT) {
             //   model.processMessage(new PlayerStopHorz(0));
                model.processMessage(new PlayerStopHorz(model.getLocalPlayer().getId()));
            }
            if (e.getCode() == KeyCode.UP) {
             //   model.processMessage(new PlayerStopVert(0));
                model.processMessage(new PlayerStopVert(model.getLocalPlayer().getId()));
            }
            if (e.getCode() == KeyCode.DOWN) {
             //   model.processMessage(new PlayerStopVert(0));
                model.processMessage(new PlayerStopVert(model.getLocalPlayer().getId()));
            }
        });

        window.setScene(scene);
        window.show();
        animator.start();
    }
}
