package lobby;

import com.esotericsoftware.minlog.Log;
import game.singlePlayer.SingleplayerController;
import images.Images;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static images.Images.LOGO_IMAGE;
import static images.Images.MENU_SPASH_BG_IMAGE;

public class Main extends Application {

    private static final Font FONT = Font.font("", FontWeight.BOLD, 36);

    private static class TriCircle extends Parent {
        public TriCircle() {
            Shape shape1 = Shape.subtract(new Circle(5), new Circle(2));
            shape1.setFill(Color.WHITE);

            Shape shape2 = Shape.subtract(new Circle(5), new Circle(2));
            shape2.setFill(Color.WHITE);
            shape2.setTranslateX(5);

            Shape shape3 = Shape.subtract(new Circle(5), new Circle(2));
            shape3.setFill(Color.WHITE);
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
            text.setStyle("-fx-stroke: black; -fx-stroke-width: 2px");
            text.setEffect(new DropShadow(5, 0, 5, Color.BLACK));

            getChildren().addAll(c1, text, c2);
            setActive(false);
            setOnActivate(() -> System.out.println(name + " activated"));
        }

        public void setActive(boolean b) {
            c1.setVisible(b);
            c2.setVisible(b);
            text.setFill(b ? Color.WHITE : Color.LIGHTBLUE);
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
        Image logo = LOGO_IMAGE;
        window.getIcons().add(logo);
        window.setResizable(true);
        window.setMinHeight(600);
        window.setMinWidth(800);
        window.setWidth(bounds.getWidth() - 50);
        window.setHeight(bounds.getHeight() - 50);
        window.setX(25);
        window.setY(25);
        window.setTitle("King Slayer");
        //window.setFullScreen(true);


        Canvas bgCanvas = new Canvas();
        GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();
        Canvas midCanvas = new Canvas();
        GraphicsContext midGC = midCanvas.getGraphicsContext2D();

        MenuItem[] items = new MenuItem[]{
            new MenuItem("JOIN GAME"),
            new MenuItem("NEW GAME"),
            new MenuItem("TEST GAME"),
            new MenuItem("HOW TO PLAY"),
            new MenuItem("EXIT")};

        items[0].setOnActivate(() -> System.out.println("JOIN GAME"));
        items[1].setOnActivate(() -> System.out.println("NEW GAME"));
        items[2].setOnActivate(() -> {
            try {
                new SingleplayerController().start(window);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        items[3].setOnActivate(() -> System.out.println("HOW TO PLAY"));
        items[4].setOnActivate(() -> System.exit(0));
        menuBox = new VBox(10, items);
        items[0].setActive(true);

        InvalidationListener resize = l -> {
            //  if(window.isMaximized()) {
            //      window.setHeight(bounds.getHeight());
            //      window.setWidth(bounds.getWidth());
            //  }
            bgCanvas.setWidth(window.getWidth());
            bgCanvas.setHeight(window.getHeight());
            midCanvas.setWidth(window.getWidth());
            midCanvas.setHeight(window.getHeight());
            Log.info("w/h = " + window.getWidth() + " " + window.getHeight());
        };

        window.heightProperty().addListener(resize);
        window.widthProperty().addListener(resize);
        window.maximizedProperty().addListener(resize);

        Group root = new Group();
        root.getChildren().add(bgCanvas);
        root.getChildren().add(midCanvas);
        root.getChildren().add(menuBox);
        Scene scene = new Scene(root);

        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
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

        double[] bgOpac = new double[]{2.0};

        AnimationTimer animator = new AnimationTimer() {

            @Override
            public void handle(long now) {
                midGC.clearRect(0, 0, window.getWidth(), window.getHeight());
                midGC.setFill(Color.color(0.6, 0.6, 0.7, 0.4 + (Math.abs(bgOpac[0] % 2 - 1)) * 0.3));
                bgOpac[0] += 0.002;

                midGC.fillRect(0, 0, window.getWidth(), window.getHeight());
                midGC.drawImage(logo, 50, 50, 250, 250);

                bgGC.drawImage(MENU_SPASH_BG_IMAGE, 0, 0, window.getWidth(), window.getHeight());

                menuBox.setTranslateX(window.getWidth() / 2 - menuBox.getWidth() / 2);
                menuBox.setTranslateY(window.getHeight() - menuBox.getHeight() - 200);
            }
        };

        animator.start();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        resize.invalidated(null);
        window.setScene(scene);
        window.show();

/*
        window.on
        Group root = new Group();
        Scene scene = new Scene(root);

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
