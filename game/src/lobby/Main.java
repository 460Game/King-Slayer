package lobby;

import com.esotericsoftware.minlog.Log;
import game.SinglePlayer.CopyingModelWrapper;
import game.SinglePlayer.SingleplayerController;
import game.model.Game.Model.ClientGameModel;
import game.model.Game.Model.ServerGameModel;
import game.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Collections;

public class Main extends Application {

    private Stage window;
    private Scene scene;

    @Override
    public void start(Stage window) {
        this.window = window;

        window.show();
Image logo = new Image(Main.class.getResourceAsStream("logo.png"));
        SingleplayerController test = new SingleplayerController();
        window.getIcons().add(logo);
        window.setMinHeight(400);
        window.setMinWidth(600);
        window.setResizable(true);
        window.setTitle("King Slayer");
      //  window.setFullScreen(true);

        Group root = new Group();


        Canvas bgCanvas = new Canvas(window.getWidth(), window.getHeight());
        GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();


        Canvas midCanvas = new Canvas(window.getWidth(), window.getHeight());
        GraphicsContext midGC = midCanvas.getGraphicsContext2D();

        //  Button joinGame = new Button("button");
        //  root.getChildren().add(joinGame);

        root.getChildren().add(bgCanvas);
        root.getChildren().add(midCanvas);
        scene = new Scene(root);

        midGC.setFill(Color.color(0.4,0.4,0.5,0.8));
        midGC.fillRect(0,0,10000000,10000000);
        midGC.drawImage(logo, 100, 100, 300, 300);

        Image bgImage = new Image(Main.class.getResourceAsStream("bg.png"));

        window.heightProperty().addListener(l -> {
            bgCanvas.setHeight(window.getHeight());
            bgGC.drawImage(bgImage,0,0, bgCanvas.getWidth(), bgCanvas.getHeight());
        });

        window.widthProperty().addListener(l -> {
            bgCanvas.setWidth(window.getWidth());
            bgGC.drawImage(bgImage,0,0, bgCanvas.getWidth(), bgCanvas.getHeight());
        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) window.setFullScreen(true);
        });

        scene.setOnMouseClicked(e -> {

            try {
                test.start(window);
            } catch (Exception er) {
                er.printStackTrace();
            }
        });

        bgGC.drawImage(bgImage,0,0, bgCanvas.getWidth(), bgCanvas.getHeight());
        bgGC.setTransform(new Affine(Affine.scale(10,10)));

        bgGC.transform(new Affine(Affine.translate(0,bgCanvas.getHeight()/2)));

        AnimationTimer animator = new AnimationTimer() {

            @Override
            public void handle(long now) {
//                bgGC.transform(new Affine(Affine.translate(10,0)));
            }
        };

        animator.start();
        window.show();
        window.setScene(scene);


/*
        window.on
        Group root = new Group();
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.F11) window.setFullScreen(true);
            });
        window.show();

        LobbyModel model = new LobbyModel();
        serverModel.init(Collections.singleton(new CopyingModelWrapper(clientModel)));
        serverModel.start();
        clientModel.start();
        GameView gameView = new GameView(clientModel);
        gameView.start(primaryStage);*/
    }


    public static void main(String args[]) {
        Application.launch();
    }

}
