package lobby;

import com.esotericsoftware.minlog.Log;
import game.singlePlayer.SingleplayerController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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
import network.Lobby;
import network.LobbyClient;
import network.LobbyServer;

import static images.Images.*;
import static util.Util.sleep;

public class Main extends Application {
    LobbyClient lobbyClient = null;
    LobbyServer lobbyServer = null;

    private Font font = Font.font("", FontWeight.BOLD, 36);
    private static Color textColor = Color.web("b5de0f");

    private VBox menuBox;

    private int currentItem = 0;
    private AnimationTimer animator;

    MenuItem[] items = new MenuItem[]{
        new MenuItem("JOIN GAME"),
        new MenuItem("NEW GAME"),
        new MenuItem("TEST GAME"),
        new MenuItem("HOW TO PLAY"),
        new MenuItem("EXIT")};

    private class TriCircle extends Parent {
        private TriCircle() {
            Shape shape1 = Shape.subtract(new Circle(8), new Circle(2));
            shape1.setFill(textColor);

            Shape shape2 = Shape.subtract(new Circle(8), new Circle(2));
            shape2.setFill(textColor);
            shape2.setTranslateX(5);

            Shape shape3 = Shape.subtract(new Circle(8), new Circle(2));
            shape3.setFill(textColor);
            shape3.setTranslateX(2.5);
            shape3.setTranslateY(-5);

            getChildren().addAll(shape1, shape2, shape3);

            setEffect(new GaussianBlur(2));
        }
    }

    private class MenuItem extends HBox {
        private TriCircle c1 = new TriCircle(), c2 = new TriCircle();
        private Text text;
        private Runnable script;

        private MenuItem(String name) {
            super(15);
            setAlignment(Pos.CENTER);

            text = new Text(name);
            text.setFont(font);
            text.setStyle("-fx-stroke: black; -fx-stroke-width: 2px");
            text.setEffect(new DropShadow(5, 0, 5, Color.BLACK));

            getChildren().addAll(c1, text, c2);
            setActive(false);
            setOnActivate(() -> System.out.println(name + " activated"));

            this.setOnMouseClicked(l -> {
                this.activate();
            });

            this.setOnMouseMoved(l -> {
                int i = 0;
                for(MenuItem m : items) {
                    if(m == this)
                        currentItem = i;
                    else
                        m.setActive(false);
                    i++;
                }
                this.setActive(true);
            });
        }

        private void updateSize() {
            text.setFont(font);
        }

        public void setActive(boolean b) {
            c1.setVisible(b);
            c2.setVisible(b);
            text.setFill(b ? Color.WHITE : textColor);
        }

        public void setOnActivate(Runnable r) {
            script = r;
        }

        public void activate() {
            if (script != null)
                script.run();
        }
    }

    private Stage window;

    @Override
    public void start(Stage window) {
        this.window = window;
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        window.getIcons().add(LOGO_IMAGE);
        window.setResizable(true);
        window.setMinHeight(400);
        window.setMinWidth(600);
        window.setWidth(bounds.getWidth() - 50);
        window.setHeight(bounds.getHeight() - 50);
        window.setX(25);
        window.setY(25);
        window.setTitle("King Slayer");
        window.setFullScreen(true);

        Canvas bgCanvas = new Canvas();
        GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();
        Canvas midCanvas = new Canvas();
        GraphicsContext midGC = midCanvas.getGraphicsContext2D();

        items[0].setOnActivate(this::joinGame);
        items[1].setOnActivate(this::startGame);
        items[2].setOnActivate(this::testGame);
        items[3].setOnActivate(this::howToPlay);
        items[4].setOnActivate(this::exit);
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
            font = Font.font("", FontWeight.BOLD, window.getHeight()/400 * 18);
            for(MenuItem item : items)
                item.updateSize();
        };

        window.heightProperty().addListener(resize);
        window.widthProperty().addListener(resize);
        window.maximizedProperty().addListener(resize);

        Group root = new Group();
        root.getChildren().add(bgCanvas);
        root.getChildren().add(midCanvas);
        root.getChildren().add(menuBox);
        Scene scene = new Scene(root);


        scene.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));

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
                case SPACE:
                    items[currentItem].activate();
                    break;
            }
        });

        double[] bgOpac = new double[]{2.0};

        animator = new AnimationTimer() {

            @Override
            public void handle(long now) {
                midGC.clearRect(0, 0, window.getWidth(), window.getHeight());
                midGC.setFill(Color.color(0.6, 0.6, 0.7, 0.4 + (Math.abs(bgOpac[0] % 2 - 1)) * 0.3));
                bgOpac[0] += 0.002;

                midGC.fillRect(0, 0, window.getWidth(), window.getHeight());
                midGC.drawImage(LOGO_TEXT_IMAGE, window.getWidth()/4, 50, window.getWidth()/2, (153/645.0)*window.getWidth()/2);

                bgGC.drawImage(MENU_SPASH_BG_IMAGE, 0, 0, window.getWidth(), window.getHeight());

                menuBox.setTranslateX(window.getWidth() / 2 - menuBox.getWidth() / 2);
                menuBox.setTranslateY(window.getHeight() - menuBox.getHeight() - 100);
            }
        };

        animator.start();
        resize.invalidated(null);
        sleep(100);
        window.setScene(scene);
        window.show();
    }

    private void startGame() {
        lobbyServer = new LobbyServer();
        try {
            lobbyServer.start();
            lobbyClient = new LobbyClient(window);
            lobbyClient.start();
            lobbyClient.connectTo("localhost");
            Thread.sleep(2000); //(connection needs time)
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        lobbyServer.startGame();

        //hardcode here
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.info("before ready");
        ready();
    }

    public void ready() {
        lobbyClient.lobbyClientReady();
    }

    private void joinGame() {
        Log.info("JOIN GAME SELECTED");
//        lobbyClient = new LobbyClient();
//        lobbyClient.start();
//        lobbyClient.connectTo(host);
    }

    private void testGame() {
        Log.info("TEST GAME SELECTED");
        try {
            new SingleplayerController().start(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
        animator.stop();
    }

    private void howToPlay() {
        Log.info("HOW TO PLAY SELECTED");
    }

    private void exit() {
        Log.info("EXIT SELECTED");
        Platform.exit();
    }

    public static void main(String args[]) {
        Log.set(Log.LEVEL_INFO);
        Application.launch();
    }

}
