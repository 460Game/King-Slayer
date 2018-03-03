package lobby;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.singlePlayer.SingleplayerController;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import network.LobbyClient;
import network.LobbyClient2LobbyAdaptor;
import network.LobbyServer;

import java.io.IOException;
import java.lang.management.PlatformManagedObject;

import static images.Images.*;
import static util.Util.sleep;

public class Main extends Application {
    LobbyClient lobbyClient = null;
    LobbyServer lobbyServer = null;

    public Scene mainMenuScene;


    private Font font = Font.font("", FontWeight.BOLD, 36);
    private static Color textColor = Color.web("b5de0f");

    private VBox menuBox;

    private int currentItem = 0;
    private AnimationTimer animator;

    ChoiceBox<Team> teamChoice;
    ChoiceBox<Role> roleChoice;

    boolean connected = false;

    Canvas bgCanvas = new Canvas();
    GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();
    Canvas midCanvas = new Canvas();
    GraphicsContext midGC = midCanvas.getGraphicsContext2D();

    TextField playerName = new TextField();

    MenuItem[] items = new MenuItem[]{
        new MenuItem("Join LAN"),
        new MenuItem("Host LAN"),
        new MenuItem("Solo Game King"),
        new MenuItem("Solo Game Slayer"),
        new MenuItem("Options"),
        new MenuItem("Exit")};

    public void closeServer() {
        if (lobbyServer != null) {
            lobbyServer.closeServer();
        }
    }

    private class MenuItem extends HBox {
        private Text text;
        private Runnable script;

        private MenuItem(String name) {
            super(15);
            setAlignment(Pos.CENTER);

            text = new Text(name);
            text.setFont(font);
            text.setStyle("-fx-stroke: black; -fx-stroke-width: 2px");
            text.setEffect(new DropShadow(5, 0, 5, Color.BLACK));

            getChildren().addAll(text);
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

    public int restart(Stage window) {
//        window.setScene(chooseTeamAndRoleScene());
        if (lobbyClient != null) {
            System.out.println("client lobby restartFromReadyPage");
            int status = lobbyClient.restartFromReadyPage();
        }

        if (lobbyServer != null) {
            System.out.println("server lobby restartFromReadyPage");
            int status = lobbyServer.restartFromReadyPage();
        }
        return 0;
    }

    @Override
    public void start(Stage window) {
        this.window = window;
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        window.setFullScreenExitHint("");
        window.setFullScreenExitKeyCombination(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_DOWN));
        window.getIcons().add(LOGO_IMAGE);
        window.setResizable(true);
        window.setMinHeight(400);
        window.setMinWidth(600);
        window.setWidth(bounds.getWidth() - 4);
        window.setHeight(bounds.getHeight() - 4);
        window.setX(2);
        window.setY(2);
        window.setTitle("King Slayer");
      //  window.setFullScreen(true);

        items[0].setOnActivate(this::connectForm);
        items[0].setActive(true);
        items[1].setOnActivate(this::guiSetNumOfPlayer);
        items[2].setOnActivate(this::testGameKing);
        items[3].setOnActivate(this::testGameSlayer);
        items[4].setOnActivate(this::options);
        items[5].setOnActivate(this::exit);
        menuBox = new VBox(10, items);

        InvalidationListener resize = l -> {
            font = Font.font("", FontWeight.BOLD, window.getHeight()/400 * 18);
            for(MenuItem item : items)
                item.updateSize();
        };

        bgCanvas.widthProperty().bind(window.widthProperty());
        bgCanvas.heightProperty().bind(window.heightProperty());
        midCanvas.widthProperty().bind(window.widthProperty());
        midCanvas.heightProperty().bind(window.heightProperty());

        window.heightProperty().addListener(resize);
        window.widthProperty().addListener(resize);
//
        Group root = new Group();
        root.getChildren().addAll(bgCanvas, midCanvas, menuBox);
        mainMenuScene = new Scene(root);

        mainMenuScene.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));

        mainMenuScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case F11:
                    window.setFullScreen(!window.isFullScreen());
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
        Platform.runLater(()-> {
            window.setScene(mainMenuScene);
        });
        window.show();
    }

    private void connectForm() {
        Platform.runLater(() -> window.setScene(ipFormScene()));
    }

    private Scene ipFormScene() {
        Group newRoot = new Group();
        newRoot.getChildren().add(bgCanvas);
        newRoot.getChildren().add(midCanvas);
        Pane newPane = ipForm();
        newRoot.getChildren().add(newPane);

        Scene ret = new Scene(newRoot);
        ret.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));

        setAnimator(newPane);

        return ret;
    }

    public GridPane choiceTeamAndRolePane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(window.getHeight()/2, 100, window.getHeight()/2 + 100, 100));
        grid.setVgap(5);
        grid.setHgap(5);

        teamChoice = new ChoiceBox<>();
        teamChoice.getItems().add(Team.ONE);
        teamChoice.getItems().add(Team.TWO);
        teamChoice.setPrefSize(200, 60);
        teamChoice.setStyle("-fx-font: 30px \"Serif\";");
        GridPane.setConstraints(teamChoice, 0, 0);
        grid.getChildren().add(teamChoice);

        roleChoice = new ChoiceBox<>();
        roleChoice.getItems().add(Role.KING);
        roleChoice.getItems().add(Role.SLAYER);
        roleChoice.setPrefSize(200, 60);
        roleChoice.setStyle("-fx-font: 30px \"Serif\";");
        GridPane.setConstraints(roleChoice, 1, 0);
        grid.getChildren().add(roleChoice);

        playerName = new TextField();
        playerName.setPromptText("Enter player name.");
        playerName.setPrefSize(200, 60);
        playerName.setFont(Font.font(30));
        GridPane.setConstraints(playerName, 2, 0);
        grid.getChildren().add(playerName);

        Button ready = new Button("Ready");
        ready.setPrefSize(200, 60);
        ready.setFont(Font.font(20));
        GridPane.setConstraints(ready, 3, 0);
        grid.getChildren().add(ready);

        ready.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Log.info("client click ready");
                ready();
                //the following would be done in the network part

//                window.setScene(new Scene(choiceTeamAndRoleScene()));
            }

        });

        return grid;
    }

    private GridPane ipForm() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(window.getHeight()/2, 200, window.getHeight()/2 + 100, 200));
        grid.setVgap(50);
        grid.setHgap(5);

        //Defining the Name text field
        final TextField ip = new TextField();
        ip.setPrefSize(600, 80);
        ip.setPromptText("Enter ip.");
        ip.setPrefColumnCount(100);

        ip.setFont(Font.font("Verdana",30));
//        ip.fontProperty().set(Font.font(20));

//        ip.setScaleX(10);
//        ip.setScaleY(5);

//        ip.getText();
        GridPane.setConstraints(ip, 0, 0);
        grid.getChildren().add(ip);

        Button connect = new Button("Connect");
        connect.fontProperty().set(Font.font(20));

        connect.setPrefSize(200, 80);
        GridPane.setConstraints(connect, 1, 0);
        grid.getChildren().add(connect);

        connect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (connected) {
                    return;
                }
                String ipStr = ip.getText();
                try {
                    joinGame(ipStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //TODO: Change here to ping back later
                connected = true;
                //the following would be done in the network part

//                window.setScene(new Scene(choiceTeamAndRoleScene()));
            }

        });

        return grid;
    }

    public Scene inputNumOfPlayersScene() {

//        Canvas bgCanvas = new Canvas();
//        GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();
//        Canvas midCanvas = new Canvas();
//        GraphicsContext midGC = midCanvas.getGraphicsContext2D();

        Group newRoot = new Group();
        newRoot.getChildren().add(bgCanvas);
        newRoot.getChildren().add(midCanvas);
        Pane inputPane = inputNumOfPlayers();
        newRoot.getChildren().add(inputPane);

        Scene ret = new Scene(newRoot);
        ret.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));

        setAnimator(inputPane);

        return ret;
    }

    public Scene chooseTeamAndRoleScene() {

        Group newRoot = new Group();
        newRoot.getChildren().add(bgCanvas);
        newRoot.getChildren().add(midCanvas);
        Pane inputPane = choiceTeamAndRolePane();
        newRoot.getChildren().add(inputPane);

        Scene ret = new Scene(newRoot);
        ret.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));

        setAnimator(inputPane);

        return ret;
    }

    private void setAnimator(Pane addedPane) {

//        InvalidationListener resize = l -> {
//            font = Font.font("", FontWeight.BOLD, window.getHeight()/400 * 18);
//            for(MenuItem item : items)
//                item.updateSize();
//        };
//
//        window.heightProperty().addListener(resize);
//        window.widthProperty().addListener(resize);
//        window.maximizedProperty().addListener(resize);

        double[] bgOpac = new double[]{2.0};
        animator.stop();
        animator = new AnimationTimer() {

        @Override
        public void handle(long now) {
                midGC.clearRect(0, 0, window.getWidth(), window.getHeight());
                midGC.setFill(Color.color(0.6, 0.6, 0.7, 0.4 + (Math.abs(bgOpac[0] % 2 - 1)) * 0.3));
                bgOpac[0] += 0.002;

                midGC.fillRect(0, 0, window.getWidth(), window.getHeight());
                midGC.drawImage(LOGO_TEXT_IMAGE, window.getWidth()/4, 50, window.getWidth()/2,
                        (153/645.0)*window.getWidth()/2);

                bgGC.drawImage(MENU_SPASH_BG_IMAGE, 0, 0, window.getWidth(), window.getHeight());

                addedPane.setTranslateX(window.getWidth() / 2 - addedPane.getWidth() / 2);
                addedPane.setTranslateY(window.getHeight() - addedPane.getHeight() + 200);
            }
        };

        animator.start();
    }

    private GridPane inputNumOfPlayers() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(window.getHeight()/2, 100, window.getHeight()/2 + 100, 100));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Name text field
        final TextField numOfPlayer = new TextField();
        numOfPlayer.setPromptText("Enter Number Of Players.");
        numOfPlayer.setPrefColumnCount(100);
        numOfPlayer.setPrefSize(800, 60);
        numOfPlayer.setFont(Font.font ("Verdana", 30));

        GridPane.setConstraints(numOfPlayer, 0, 0);
        grid.getChildren().add(numOfPlayer);

        Button set = new Button("set");
        set.setPrefSize(100, 60);
        set.setFont(Font.font ("Verdana", 20));
        GridPane.setConstraints(set, 1, 0);
        grid.getChildren().add(set);

        set.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lobbyServer.setNumOfPlayers(Integer.parseInt(numOfPlayer.getText()));
                startGame();
                //the following would be done in the network part

//                window.setScene(new Scene(choiceTeamAndRoleScene()));
            }

        });

        return grid;
    }

    private void guiSetNumOfPlayer() {
        lobbyServer = new LobbyServer();
        try {
            lobbyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        lobbyClient = new LobbyClient(window, new LobbyClient2LobbyAdaptor() {
            @Override
            public void showChoiceTeamAndRoleScene() {
                System.err.println("Set team and role scene again");
                Task show = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Platform.runLater(()->window.setScene(chooseTeamAndRoleScene()));
                        return null;
                    }
                };
                new Thread(show).start();
            }
        }, this);

//        window.getScene().getRoot().getChildrenUnmodifiable().remove(0, 1);

        Platform.runLater(() -> window.setScene(inputNumOfPlayersScene()));
//        Platform.runLater(() -> window.setScene(new Scene(inputNumOfPlayers())));
    }

    private void startGame() {

        try {

            lobbyClient.start();
            lobbyClient.connectTo("localhost");
            //TODO: change this to Ping back later
            Thread.sleep(2000); //(connection needs time)
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    public void ready() {
        //TODO: get from the view later!!!!!!!!!!!
        Team team = teamChoice.getValue();
        Role role = roleChoice.getValue();
        String playerNameStr = playerName.getText();
        System.out.println("player name is " + playerNameStr + " " + team + role);
        lobbyClient.lobbyClientReady(team, role, playerNameStr);
    }

    private void joinGame(String host) throws Exception {
        Log.info("JOIN GAME SELECTED");
        lobbyClient = new LobbyClient(window, new LobbyClient2LobbyAdaptor() {
            @Override
            public void showChoiceTeamAndRoleScene() {
                Platform.setImplicitExit(false);
                Platform.runLater(() -> window.setScene(chooseTeamAndRoleScene()));
//                window.setScene(new Scene(choiceTeamAndRoleScene()));
            }
        }, this);
        lobbyClient.start();
        lobbyClient.connectTo(host);
        //TODO: change this to ping back later
        Thread.sleep(2000);
    }

    private void testGameSlayer() {
        Log.info("TEST GAME SELECTED");
        try {
            new SingleplayerController(this, Role.SLAYER).start(window);
        } catch (Exception e) {
            e.printStackTrace();
        }
        animator.stop();
    }

    private void testGameKing() {
        Log.info("TEST GAME SELECTED");
        try {
            new SingleplayerController(this, Role.KING).start(window);
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


    private void options() {
        Log.info("OPTIONS SELECTED");
    }
}
