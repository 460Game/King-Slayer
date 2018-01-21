package game.view;

import static Util.Const.*;
import com.esotericsoftware.minlog.Log;

import game.message.playerMoveMessage.*;
import game.model.Game.Model.ClientGameModel;
import game.model.Game.WorldObject.Entity.Entity;
import game.model.Game.WorldObject.Entity.TestPlayer;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.awt.*;

public class ClientView {

    private ClientGameModel model;

    public ClientView(ClientGameModel model) {
        this.model = model;
    }

    public void start(Stage window) {

        window.setResizable(true);
        window.setTitle("King Slayer");

        Group root = new Group();
        Canvas canvas = new Canvas(INIT_SCREEN_WIDTH, INIT_SCREEN_HEIGHT);
        Canvas minimapCanvas = new Canvas(INIT_SCREEN_WIDTH/3, INIT_SCREEN_HEIGHT/3);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext minimapGC = minimapCanvas.getGraphicsContext2D();

        window.widthProperty().addListener(l -> {
            minimapCanvas.setWidth(Math.min(window.getWidth()/3, window.getHeight()/3));
            minimapCanvas.setHeight(Math.min(window.getWidth()/3, window.getHeight()/3));
            canvas.setWidth(window.getWidth());
        });

        window.heightProperty().addListener(l -> {
            minimapCanvas.setWidth(Math.min(window.getWidth()/3, window.getHeight()/3));
            minimapCanvas.setHeight(Math.min(window.getWidth()/3, window.getHeight()/3));
            canvas.setHeight(window.getHeight());
        });

        window.setFullScreen(true);

        root.getChildren().add(canvas);
        root.getChildren().add(minimapCanvas);


        Scene scene = new Scene(root);

        double[] scaleFactor = {5};

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                minimapGC.setTransform(new Affine(Transform.scale(minimapGC.getCanvas().getWidth()/model.getMapWidth(),
                    minimapGC.getCanvas().getHeight()/model.getMapHeight())));
                minimapGC.fillRect(0,0,minimapGC.getCanvas().getWidth(), minimapGC.getCanvas().getHeight());
                for(int x= 0; x < model.getMapWidth(); x++){
                    for(int y =0; y< model.getMapHeight();y++) {
                        minimapGC.setFill(model.getTile(x,y).getColor());
                        minimapGC.fillRect(x,y,2,2);
                    }
                }
                //TEMP HACK
                for(Entity player : model.getAllEntities()) {
                    if(player instanceof TestPlayer) {
                        minimapGC.setFill(player.getTeam().color);
                        minimapGC.fillOval(player.getX(),player.getY(),2,2);
                    }
                }
                minimapGC.setTransform(new Affine());

                double gameW = scaleFactor[0] * window.getWidth() / TILE_PIXELS;
                double gameH = scaleFactor[0] * window.getHeight() / TILE_PIXELS;
                double xt = - model.getLocalPlayer().getX() * TILE_PIXELS + window.getWidth() / 2;
                double yt = -model.getLocalPlayer().getY() * TILE_PIXELS + (window.getHeight() / 2);
                gc.transform(new Affine(Affine.translate(xt, yt)));
                model.draw(gc, model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), gameW, gameH);
                gc.transform(new Affine(Affine.translate(-xt, -yt)));
            }
        };

        scene.setOnScroll(e -> {
            if (e.getDeltaY() < 0) {
                scaleFactor[0] *= 1.1;
                gc.transform(new Affine(Affine.scale(0.9, 0.9)));
            } else {
                scaleFactor[0] *= 0.9;
                gc.transform(new Affine(Affine.scale(1.1, 1.1)));
            }
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) window.setFullScreen(true);
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W)
                model.processMessage(new PlayerUp(model.getLocalPlayer().getId()));
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S)
                model.processMessage(new PlayerDown(model.getLocalPlayer().getId()));
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A)
                model.processMessage(new PlayerLeft(model.getLocalPlayer().getId()));
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D)
                model.processMessage(new PlayerRight(model.getLocalPlayer().getId()));
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D)
                model.processMessage(new PlayerStopHorz(model.getLocalPlayer().getId()));
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A)
                model.processMessage(new PlayerStopHorz(model.getLocalPlayer().getId()));
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W)
                model.processMessage(new PlayerStopVert(model.getLocalPlayer().getId()));
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S)
                model.processMessage(new PlayerStopVert(model.getLocalPlayer().getId()));
        });

        window.setScene(scene);
        window.show();
        animator.start();
    }
}
