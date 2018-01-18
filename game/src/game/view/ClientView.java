package game.view;

import game.message.playerMoveMessage.*;
import game.model.Game.GameModel;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

public class ClientView {

    final static int TILE_PIXELS = 32;

    final static int CANVAS_WIDTH = GameModel.GRID_X_SIZE * TILE_PIXELS;
    final static int CANVAS_HEIGHT = GameModel.GRID_Y_SIZE * TILE_PIXELS;

    final static int INIT_SCREEN_WIDTH = 800;
    final static int INIT_SCREEN_HEIGHT = 600;

    private GameModel model;

    private AnimationTimer animator;

    private Stage window;

    public ClientView(GameModel model) {
        this.model = model;
    }

    public void start(Stage window) {
        this.window = window;

        window.setResizable(true);
        window.setTitle("King Slayer");

        Group root = new Group();

        Canvas canvasBG = new Canvas(INIT_SCREEN_WIDTH, INIT_SCREEN_HEIGHT);
        Canvas canvasFG = new Canvas(INIT_SCREEN_WIDTH, INIT_SCREEN_HEIGHT);

        GraphicsContext gcFG = canvasFG.getGraphicsContext2D();
        GraphicsContext gcBG = canvasBG.getGraphicsContext2D();

        model.drawBG(gcBG);

        root.getChildren().add(canvasBG);
        root.getChildren().add(canvasFG);

        Scene scene = new Scene(root);

        double scaleFactor = 1;

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                model.update();
                gcFG.clearRect(0, 0, 100000000, 10000000);

                double gameW = scaleFactor*window.getWidth()/TILE_PIXELS;
                double gameH = scaleFactor*window.getHeight()/TILE_PIXELS;

                model.drawFG(gcFG, model.playerB.getX()- gameW/2, model.playerB.getY() - gameH/2, gameW, gameH);
            }
        };

        scene.setOnScroll(e -> {
            System.out.println("Scroll event, delta is " + e.getDeltaY());
            if(e.getDeltaY() < 0 ) {
                gcFG.transform(new Affine(Affine.scale(0.9, 0.9)));
                gcBG.transform(new Affine(Affine.scale(0.9, 0.9)));
                model.drawBG(gcBG);
            } else {
                gcFG.transform(new Affine(Affine.scale(1.1, 1.1)));
                gcBG.transform(new Affine(Affine.scale(1.1, 1.1)));
                model.drawBG(gcBG);
            }
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.S) {
                gcFG.transform(new Affine(Affine.translate(0, -100)));
                gcBG.transform(new Affine(Affine.translate(0, -100)));
                model.drawBG(gcBG);
            }
            //   model.processMessage(new PlayerUp(0));
            if (e.getCode() == KeyCode.W) {

                gcFG.transform(new Affine(Affine.translate(0, 100)));
                gcBG.transform(new Affine(Affine.translate(0, 100)));
                model.drawBG(gcBG);
            }
            //   model.processMessage(new PlayerDown(0));
            if (e.getCode() == KeyCode.D) {

                gcFG.transform(new Affine(Affine.translate(-100, 0)));
                gcBG.transform(new Affine(Affine.translate(-100, 0)));
                model.drawBG(gcBG);
            }
            //  model.processMessage(new PlayerLeft(0));
            if (e.getCode() == KeyCode.A) {

                gcFG.transform(new Affine(Affine.translate(100, 0)));
                gcBG.transform(new Affine(Affine.translate(100, 0)));
                model.drawBG(gcBG);
            }
            //   model.processMessage(new PlayerRight(0));
            //           model.playerA.right();
            if (e.getCode() == KeyCode.UP)
                model.processMessage(new PlayerUp(1));
            if (e.getCode() == KeyCode.DOWN)
                model.processMessage(new PlayerDown(1));
            if (e.getCode() == KeyCode.LEFT)
                model.processMessage(new PlayerLeft(1));
            if (e.getCode() == KeyCode.RIGHT)
                model.processMessage(new PlayerRight(1));
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W)
                model.processMessage(new PlayerStopVert(0));
            //     model.playerA.stopVert();
            if (e.getCode() == KeyCode.S)
                model.processMessage(new PlayerStopVert(0));
            //        model.playerA.stopVert();
            if (e.getCode() == KeyCode.A)
                model.processMessage(new PlayerStopHorz(0));
            //         model.playerA.stopHorz();
            if (e.getCode() == KeyCode.D)
                model.processMessage(new PlayerStopHorz(0));
            //         model.playerA.stopHorz();
            if (e.getCode() == KeyCode.RIGHT)
                model.processMessage(new PlayerStopHorz(1));
            if (e.getCode() == KeyCode.LEFT)
                model.processMessage(new PlayerStopHorz(1));
            if (e.getCode() == KeyCode.UP)
                model.processMessage(new PlayerStopVert(1));
            if (e.getCode() == KeyCode.DOWN)
                model.processMessage(new PlayerStopHorz(1));
        });

        window.setScene(scene);
        window.show();
        animator.start();
    }
}
