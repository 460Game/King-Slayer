package game.view;

import game.message.playerMoveMessage.*;
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

    final static int SCREEN_WIDTH = 1000;
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
                model.processMessage(new PlayerUp(0));
            if (e.getCode() == KeyCode.S)
                model.processMessage(new PlayerDown(0));
            if (e.getCode() == KeyCode.A)
                model.processMessage(new PlayerLeft(0));
            if (e.getCode() == KeyCode.D)
                model.processMessage(new PlayerRight(0));
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
