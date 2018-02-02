package lobby;

import com.esotericsoftware.minlog.Log;
import game.SinglePlayer.CopyingModelWrapper;
import game.SinglePlayer.SingleplayerController;
import game.model.Game.Model.ClientGameModel;
import game.model.Game.Model.ServerGameModel;
import game.view.GameView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Collections;
import java.util.function.Function;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private static final Font FONT = Font.font("", FontWeight.BOLD, 36);

    private static class TriCircle extends Parent {
        public TriCircle() {
            Shape shape1 = Shape.subtract(new Circle(5), new Circle(2));
            shape1.setFill(Color.PEACHPUFF);

            Shape shape2 = Shape.subtract(new Circle(5), new Circle(2));
            shape2.setFill(Color.PEACHPUFF);
            shape2.setTranslateX(5);

            Shape shape3 = Shape.subtract(new Circle(5), new Circle(2));
            shape3.setFill(Color.PEACHPUFF);
            shape3.setTranslateX(2.5);
            shape3.setTranslateY(-5);

            getChildren().addAll(shape1, shape2, shape3);

            setEffect(new GaussianBlur(2));
        }
    }

    private static class MenuItem extends HBox {
        private TriCircle c1 = new TriCircle(), c2 = new TriCircle();
        private Text text;
        private Runnable script;

        public MenuItem(String name) {
            super(15);
            setAlignment(Pos.CENTER);

            text = new Text(name);
            text.setFont(FONT);
            text.setEffect(new GaussianBlur(2));
            text.setEffect(new DropShadow());

            getChildren().addAll(c1, text, c2);
            setActive(false);
            setOnActivate(() -> System.out.println(name + " activated"));
        }

        public void setActive(boolean b) {
            c1.setVisible(b);
            c2.setVisible(b);
            text.setFill(b ? Color.MEDIUMPURPLE : Color.LIGHTGREEN);
        }

        public void setOnActivate(Runnable r) {
            script = r;
        }

        public void activate() {
            if (script != null)
                script.run();
        }
    }

    private int currentItem = 0;
    @Override
    public void start(Stage window) {

        VBox menuBox;

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        window.show();
        Image logo = new Image(Main.class.getResourceAsStream("logo.png"));
        SingleplayerController test = new SingleplayerController();
        window.getIcons().add(logo);
        window.setResizable(true);
        window.setMinHeight(800);
        window.setMinWidth(1200);
        window.setWidth(bounds.getWidth());
        window.setHeight(bounds.getHeight());
        window.setX(0);
        window.setY(0);
        window.setTitle("King Slayer");
      //  window.setFullScreen(true);

        Group root = new Group();


        Canvas bgCanvas = new Canvas(window.getWidth(), window.getHeight());
        GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();


        Canvas midCanvas = new Canvas(window.getWidth(), window.getHeight());
        GraphicsContext midGC = midCanvas.getGraphicsContext2D();



        MenuItem[] items = new MenuItem[] {
            new MenuItem("JOIN LAN GAME"),
            new MenuItem("NEW LAN GAME"),
            new MenuItem("QUICK SOLO GAME"),
            new MenuItem("HOW TO PLAY"),
            new MenuItem("EXIT")};

        items[0].setOnActivate(() -> System.out.println("JOIN GAME"));
        items[1].setOnActivate(() -> System.out.println("NEW GAME"));
        items[2].setOnActivate(() -> {
            try {
                test.start(window);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        items[3].setOnActivate(() -> System.out.println("HOW TO PLAY"));
        items[4].setOnActivate(() -> System.exit(0));
        menuBox = new VBox(10, items);
        menuBox.setTranslateX(window.getWidth() - menuBox.getWidth() - 200);
        menuBox.setTranslateY(window.getHeight() - menuBox.getHeight()/2);
        items[0].setActive(true);

        //  Button joinGame = new Button("button");
        //  root.getChildren().add(joinGame);

        root.getChildren().add(bgCanvas);
        root.getChildren().add(midCanvas);
        root.getChildren().add(menuBox);
        Scene scene = new Scene(root);

        midGC.setFill(Color.color(0.4,0.4,0.5,0.8));
        midGC.fillRect(0,0,10000000,10000000);
        midGC.drawImage(logo, 50, 50, 150, 150);

        Image bgImage = new Image(Main.class.getResourceAsStream("bg.png"));

        InvalidationListener resize = l -> {
            if(window.isMaximized()) {
                window.setHeight(bounds.getHeight());
                window.setWidth(bounds.getWidth());
            }
            bgCanvas.setWidth(window.getWidth());
            bgCanvas.setHeight(window.getHeight());
            bgGC.drawImage(bgImage,0,0, window.getWidth(), window.getHeight());
            menuBox.setTranslateX(window.getWidth()/2 - menuBox.getWidth()/2);
            menuBox.setTranslateY(window.getHeight() - menuBox.getHeight() - 200);
        };

        window.heightProperty().addListener(resize);
        window.widthProperty().addListener(resize);
        window.maximizedProperty().addListener(resize);
        resize.invalidated(null);

        scene.setOnKeyPressed(e -> {
            switch(e.getCode()) {
                case F11:
                    window.setFullScreen(true);
                    break;
                case UP:
                case W:
                    if (currentItem > 0) {
                        items[currentItem].setActive(false);
                        items[--currentItem].setActive(true);
                    }
                    break;
                case DOWN:
                case S:
                    if (currentItem < menuBox.getChildren().size() - 1) {
                        items[currentItem].setActive(false);
                        items[++currentItem].setActive(true);
                    }
                    break;
                case ENTER:
                    items[currentItem].activate();
                    break;
            }
        });

        scene.setOnMouseClicked(e -> {

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
