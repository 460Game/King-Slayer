package game.view;

import game.model.Game.GameModel;
import game.model.Game.WorldObject.TestPlayer;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class ClientView {

    final static int SCREEN_WIDTH = 1500;
    final static int SCREEN_HEIGHT = 1000;

    private GameModel model;
    private AnimationTimer animator;
    private Stage window;

    public ClientView(GameModel model) {
        this.model = model;
    }

    public void start(Stage window) {
        this.window = window;

        window.setResizable(false);
        window.setTitle("King Slayer");

        Group root = new Group();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                model.update();
                model.draw(gc);
            }
        };
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.W)
                model.playerA.up();
            if (e.getCode() == KeyCode.S)
                model.playerA.down();
            if (e.getCode() == KeyCode.A)
                model.playerA.left();
            if (e.getCode() == KeyCode.D)
                model.playerA.right();
            if (e.getCode() == KeyCode.UP)
                model.playerB.up();
            if (e.getCode() == KeyCode.DOWN)
                model.playerB.down();
            if (e.getCode() == KeyCode.LEFT)
                model.playerB.left();
            if (e.getCode() == KeyCode.RIGHT)
                model.playerB.right();
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W)
                model.playerA.stopVert();
            if (e.getCode() == KeyCode.S)
                model.playerA.stopVert();
            if (e.getCode() == KeyCode.A)
                model.playerA.stopHorz();
            if (e.getCode() == KeyCode.D)
                model.playerA.stopHorz();
            if (e.getCode() == KeyCode.RIGHT)
                model.playerB.stopHorz();
            if (e.getCode() == KeyCode.LEFT)
                model.playerB.stopHorz();
            if (e.getCode() == KeyCode.UP)
                model.playerB.stopVert();
            if (e.getCode() == KeyCode.DOWN)
                model.playerB.stopVert();
        });

        window.setScene(scene);
        window.show();
        animator.start();
    }
}
