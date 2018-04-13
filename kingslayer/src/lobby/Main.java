package lobby;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.team.Role;
import game.model.game.model.team.Team;
import game.singlePlayer.SingleplayerController;
import images.Images;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import music.MusicPlayer;
import network.LobbyClient;
import network.LobbyClient2LobbyAdaptor;
import network.LobbyServer;
import util.CssSheet;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static images.Images.*;
import static util.Util.random;
import static util.Util.sleep;

public class Main extends Application {
    LobbyClient lobbyClient = null;
    LobbyServer lobbyServer = null;

    public Scene mainMenuScene;
    TextField nameOfPlayer;

    private Font font = Font.font("", FontWeight.BOLD, 36);
    private static Color textColor = Color.web("b5de0f");

    private VBox menuBox;

    private int currentItem = 0;
    private AnimationTimer animator;
    Button ready;

    ChoiceBox<Team> teamChoice;
    ChoiceBox<Role> roleChoice;

    int numOnTeam;

    boolean connected = false;

    Canvas bgCanvas = new Canvas();
    GraphicsContext bgGC = bgCanvas.getGraphicsContext2D();
    Canvas midCanvas = new Canvas();
    GraphicsContext midGC = midCanvas.getGraphicsContext2D();
    private boolean playerNumAlreadySet = false;
    TextField playerName = new TextField();
    ChoiceBox numChoice;


    Button selectRedKing;
    Button selectBlueKing;

    Button slayerSelect[][] = new Button[2][4];

    Button selectRedSl;
    Button selectBlueSl;

    Button selectRedSl2;
    Button selectBlueSl2;

    Button selectRedSl3;
    Button selectBlueSl3;

    Thread findHostThread;

    AtomicReference<CustomMenuItem> hostDropDownItem = new AtomicReference(null);

    AtomicReference<Text> hostDropDownText = new AtomicReference(null);

    boolean isServer = false;

    MenuItem[] items = new MenuItem[] {
        new MenuItem("Join LAN"),
        new MenuItem("Host LAN"),
        new MenuItem("Solo Game King"),
        new MenuItem("Solo Game Slayer"),
        new MenuItem("Options"),
        new MenuItem("Exit")};


    public int closeServer() {
        if (lobbyServer != null) {
            return lobbyServer.closeServer();
        }
        if (lobbyClient != null) {
            return lobbyClient.closeClientLobby();
        }
        return 0;
    }

    public void allClientReady() {
        Platform.runLater(() -> {
            ready.setText("Loading...");
        });
    }

    public void readyButtonFb(boolean status) {
        if (Platform.isFxApplicationThread()) {
            if (status) {
                ready.setText("Waiting...");

            } else {
                ready.setText("NEED TO SELECT");
                //TODO: fail to ready
            }
            return;
        }
        Platform.runLater(() -> {
            if (status) {
                ready.setText("Waiting...");
            } else {
                ready.setText("NEED TO SELECT");
                //TODO: fail to ready
            }
        });
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

    public int restartFromMainMenu() {
        cleanup();
        startMain(this.window);
        MusicPlayer.playIntroMusic();
        return 0;
    }

    private void cleanup() {
        //seems like everything would be reset later, so nothing to do here
        lobbyClient = null;
        lobbyServer = null;
        bgCanvas = new Canvas();
        bgGC = bgCanvas.getGraphicsContext2D();
        midCanvas = new Canvas();
        midGC = midCanvas.getGraphicsContext2D();
        playerNumAlreadySet = false;
        playerName = new TextField();
    }

    public int rematch() {
//        if (lobbyServer != null) {
//            lobbyServer.lobbyServerRematch() {
//
//            }
//        }
        if (lobbyClient != null) {
            lobbyClient.lobbyClientRematch();
        }
        return 0;
    }

    public int singleplayerRestart() {
        return 0;
    }

    @Override
    public void start(Stage window) {
        startMain(window);
    }

    private void connectForm() {
        lobbyClient = new LobbyClient(window, new LobbyClient2LobbyAdaptor() {
            @Override
            public void showChoiceTeamAndRoleScene() {
                Platform.setImplicitExit(false);
                Log.info("set chooseTeamAndRoleScene scene");
                Platform.runLater(() -> window.setScene(chooseTeamAndRoleScene()));
//                window.setScene(new Scene(choiceTeamAndRoleScene()));
            }

            @Override
            public void takeSelectFb(boolean s, Map<Integer, PlayerInfo> map) {
                takeFb(s, map);
            }

            @Override
            public void setNumOnTeam(int numOnTeam) {
                Main.this.numOnTeam = numOnTeam;
            }

            @Override
            public void roleReadyLock(PlayerInfo info) {
                readyLockLocalMethod(info);
            }
        }, this);

        try {
            lobbyClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }


        findHostThread = new Thread(() -> {
            while (!connected) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (lobbyClient == null) continue;
                InetAddress addr = lobbyClient.discoverHost();
                System.out.println(addr);
                if (addr != null) {
//                    ((Text)(hostDropDownItem.get().getContent())).setText(String.valueOf(random.nextInt()));

                    ((Text)(hostDropDownItem.get().getContent())).setText(addr.toString().split("/")[1]);

//                    Text replace = new Text(addr.toString().split("/")[1]);
//                    replace.setFont(Font.font("", FontWeight.BOLD, 25));
//                    hostDropDownText.getAndSet(replace);
                }
            }
        });
        findHostThread.start();

        Platform.runLater(() -> window.setScene(ipFormScene()));
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

//        double[] bgOpac = new double[]{2.0};
//        animator.stop();
//        animator = new AnimationTimer() {
//
//        @Override
//        public void handle(long now) {
//                midGC.clearRect(0, 0, window.getWidth(), window.getHeight());
//                midGC.setFill(Color.color(0.6, 0.6, 0.7, 0.4 + (Math.abs(bgOpac[0] % 2 - 1)) * 0.3));
//                bgOpac[0] += 0.002;
//
//                midGC.fillRect(0, 0, window.getWidth(), window.getHeight());
//                midGC.drawImage(LOGO_TEXT_IMAGE, window.getWidth()/4, 50, window.getWidth()/2,
//                        (153/645.0)*window.getWidth()/2);
//
//                bgGC.drawImage(MENU_SPASH_BG_IMAGE, 0, 0, window.getWidth(), window.getHeight());
//
//                addedPane.setTranslateX(window.getWidth() / 2 - addedPane.getWidth() / 2);
//                addedPane.setTranslateY(window.getHeight() - addedPane.getHeight() + 200);
//            }
//        };
//
//        animator.start();
    }

    private GridPane inputNumOfPlayers() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(window.getHeight()/2, 100, window.getHeight()/2 + 100, 600));
        grid.setVgap(5);
        grid.setHgap(5);

        //Defining the Name text field
        nameOfPlayer = new TextField();
        nameOfPlayer.setPromptText("Enter your name.");
//        numOfPlayer.setText("Default Player");
        nameOfPlayer.setPrefColumnCount(100);
        nameOfPlayer.setPrefSize(300, 60);
        nameOfPlayer.setFont(Font.font ("Verdana", 30));

        numChoice = new ChoiceBox<>();
        numChoice.getItems().add("1v1");
        numChoice.getItems().add("2v2");
        numChoice.getItems().add("3v3");
        numChoice.getItems().add("4v4");
        numChoice.setPrefSize(200, 60);
        numChoice.setStyle("-fx-font: 30px \"Serif\";");

        //
//        grid.getChildren().add(numChoice);

        GridPane.setConstraints(nameOfPlayer, 0, 0);

        grid.getChildren().add(nameOfPlayer);

        GridPane.setConstraints(numChoice, 1, 0);

        grid.getChildren().add(numChoice);

        Button set = new Button("Set");
        set.setPrefSize(100, 60);
        set.setFont(Font.font ("Verdana", 20));
        set.setStyle(CssSheet.YELLO_BUTTON_CSS);
        GridPane.setConstraints(set, 2, 0);
        grid.getChildren().add(set);

        set.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (playerNumAlreadySet) return;
                playerNumAlreadySet = true;
//                set.setStyle(CssSheet.GREY_SELECT_BUTTON);
                set.setText("Loading...");

                numOnTeam = ((String)numChoice.getValue()).charAt(0) - '0';
                lobbyServer.setNumOfPlayersAndHostName(nameOfPlayer.getText(), numOnTeam);
                System.out.println("num player in one team: " + numOnTeam);

//                if (((String)numChoice.getValue()).startsWith("1")) {
//                    numOnTeam = 1;
//                } else {
//                    System.out.println("it is greater than 2");
//                    numOnTeam = 2;
//                }
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
                Platform.setImplicitExit(false);
                Task show = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        Log.info("set chooseTeamAndRoleScene now!!!!!");
                        Platform.runLater(()->window.setScene(chooseTeamAndRoleScene()));
                        return null;
                    }
                };
                new Thread(show).start();
            }

            @Override
            public void takeSelectFb(boolean s, Map<Integer, PlayerInfo> map) {
                takeFb(s, map);
            }

            @Override
            public void setNumOnTeam(int numOnTeam) {
                Main.this.numOnTeam = numOnTeam;
            }

            @Override
            public void roleReadyLock(PlayerInfo info) {
                readyLockLocalMethod(info);
            }
        }, this);

        try {
            lobbyClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        window.getScene().getRoot().getChildrenUnmodifiable().remove(0, 1);

        Platform.runLater(() -> window.setScene(inputNumOfPlayersScene()));
    }

    public void readyLockLocalMethod(PlayerInfo info) {
        if (info.role == Role.KING) {
            if (info.team == Team.BLUE_TEAM) {
                selectBlueKing.setStyle(CssSheet.GREY_SELECT_BUTTON);
            }
            if (info.team == Team.RED_TEAM) {
                selectRedKing.setStyle(CssSheet.GREY_SELECT_BUTTON);
            }
        }

        if (info.role == Role.SLAYER) {
            if (info.team == Team.BLUE_TEAM) {
                slayerSelect[1][info.getSlayerIdx()].setStyle(CssSheet.GREY_SELECT_BUTTON);
            }
            if (info.team == Team.RED_TEAM) {
                slayerSelect[0][info.getSlayerIdx()].setStyle(CssSheet.GREY_SELECT_BUTTON);
            }
        }
    }

    private void takeFb(boolean s, Map<Integer, PlayerInfo> map) {
        if (!s) {
            System.out.println("Fail to select role");
        }

        {
            Platform.runLater(()-> {
//                redKingLabel.setText("RED KING: ");
//                blueKingLabel.setText("BLUE KING: ");
//                if (redSlLabel != null) {//TODO: tempory, remove later
//                    redSlLabel.setText("RED SLAYER: ");
//                    blueSlLabel.setText("BLUE SLAYER: ");
//                }
                selectRedKing.setText("RED KING: SELECT");
                selectBlueKing.setText("BLUE KING: SELECT");
//                if (selectRedSl != null) {
//                    selectRedSl.setText("RED SLAYER: SELECT");
//                    selectBlueSl.setText("BLUE SLAYER: SELECT");
//                }

                for (int i = 0; i < 4; i++) {
                    if (slayerSelect[0][i] != null) {
                        slayerSelect[0][i].setText("RED SLAYER: SELECT");
                        slayerSelect[1][i].setText("BLUE SLAYER: SELECT");
                    }
                }
            });


            for (Map.Entry<Integer, PlayerInfo> entry : map.entrySet()) {
                Team team = entry.getValue().getTeam();
                Role role = entry.getValue().getRole();
                String name = entry.getValue().getPlayerName();
                int slayerIdx = entry.getValue().getSlayerIdx();
                if (team == Team.RED_TEAM && role == Role.KING) {
                    System.out.println("RED KING IS " + name);
                    Platform.runLater(() -> {
                        selectRedKing.setText(name);
                    });

                }
                else if (team == Team.RED_TEAM && role == Role.SLAYER) {
                    Platform.runLater(() -> {
//                        selectRedSl.setText(name);
                        slayerSelect[0][slayerIdx].setText(name);
                    });

                }
                else if (team == Team.BLUE_TEAM && role == Role.KING) {
                    System.out.println("BLUE KING IS " + name);
                    Platform.runLater(() -> {
//                        blueKingLabel.setText("BLUE KING: " + name);
                        selectBlueKing.setText(name);
                    });

                }
                else if (team == Team.BLUE_TEAM && role == Role.SLAYER) {
                    Platform.runLater(() -> {
//                        selectBlueSl.setText(name);
                        slayerSelect[1][slayerIdx].setText(name);
                    });

                }
            }
        }
    }

    private void startGame() {

        try {
            lobbyClient.setName(nameOfPlayer.getText());
//            lobbyClient.start();
            lobbyClient.connectTo("localhost");
            //TODO: change this to Ping back later
            Thread.sleep(2000); //(connection needs time)
            System.out.println("connected");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

    }

    public void ready() {
        //TODO: get from the view later!!!!!!!!!!!
//        Team team = teamChoice.getValue();
//        Role role = roleChoice.getValue();
        String playerNameStr = playerName.getText();
//        System.out.println("player name is " + playerNameStr + " " + team + role);
//        lobbyClient.lobbyClientReady(team, role, playerNameStr);
        lobbyClient.lobbyClientReady(playerNameStr);
    }

    private void joinGame(String host) throws Exception {
        Log.info("JOIN GAME SELECTED");

        lobbyClient.setName(playerName.getText());
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
        if (lobbyClient != null)
            lobbyClient.stop();
        if (lobbyServer != null)
            lobbyServer.closeServer();
        Log.info("EXIT SELECTED");
        Platform.exit();
        System.exit(0);
    }

    public static void main(String args[]) {
        Log.set(Log.LEVEL_INFO);
        Application.launch();
    }


    private void options() {
        Log.info("OPTIONS SELECTED");
    }

    public void startMain(Stage window_arg) {

        MusicPlayer.playIntroMusic();

        this.window = window_arg;
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
        window.setTitle("King Slayer" + random.nextInt());
        //  window.setFullScreen(true);

        window.setOnCloseRequest(e -> {
            MusicPlayer.stopMusic();
            exit();
        });

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

        window.setScene(mainMenuScene);

//        Platform.setImplicitExit(false);
//        Platform.runLater(()-> {
//
//        });
        window.show();
    }




    //Panes code
    public GridPane choiceTeamAndRolePane() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(window.getHeight()/2 - 180, 200, window.getHeight()/2 + 100, 450));
        grid.setVgap(5);
        grid.setHgap(5);


        ImageView redKingSelect = new ImageView(Images.RED_KING_SELECT);
        redKingSelect.setFitWidth(80);
        redKingSelect.setFitHeight(80);

        grid.add(redKingSelect, 0, 0, 1, 1);

        selectRedKing = new Button("RED KING: SELECT");
        selectRedKing.setPrefSize(200, 50);

        selectRedKing.setStyle(CssSheet.RED_SELECT_BUTTON);

        selectRedKing.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lobbyClient.selectRole(Team.RED_TEAM, Role.KING, -1);
            }
        });
        grid.add(selectRedKing, 1, 0, 1, 1);


        ImageView imBlueKing = new ImageView(Images.BLUE_KING_SELECT);
        imBlueKing.setFitWidth(80);
        imBlueKing.setFitHeight(80);

        grid.add(imBlueKing, 3, 0, 1, 1);

        selectBlueKing = new Button("BLUE KING: SELECT");
        selectBlueKing.setPrefSize(200, 50);
        selectBlueKing.setStyle(CssSheet.BLUE_SELECT_BUTTON);
        selectBlueKing.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lobbyClient.selectRole(Team.BLUE_TEAM, Role.KING, -1);
            }
        });
        grid.add(selectBlueKing, 4, 0, 1, 1);

        System.out.println("check num in team " + numOnTeam);
        if (numOnTeam >= 2) {
            ImageView imRedSl = new ImageView(Images.RED_SLAYER_SELECT);
            imRedSl.setFitWidth(80);
            imRedSl.setFitHeight(80);

            grid.add(imRedSl, 0, 1, 1, 1);

            slayerSelect[0][1] = new Button("RED SLAYER: SELECT");
            slayerSelect[0][1].setPrefSize(200, 50);
            slayerSelect[0][1].setStyle(CssSheet.RED_SELECT_BUTTON);
            slayerSelect[0][1].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    lobbyClient.selectRole(Team.RED_TEAM, Role.SLAYER, 1);
                }
            });
            grid.add(slayerSelect[0][1], 1, 1, 1, 1);

            ImageView imBlueSl = new ImageView(Images.BLUE_SLAYER_SELECT);
            imBlueSl.setFitWidth(80);
            imBlueSl.setFitHeight(80);
            grid.add(imBlueSl, 3, 1, 1, 1);

            slayerSelect[1][1] = new Button("BLUE SLAYER: SELECT");
            slayerSelect[1][1].setPrefSize(200, 50);
            slayerSelect[1][1].setStyle(CssSheet.BLUE_SELECT_BUTTON);
            slayerSelect[1][1].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    lobbyClient.selectRole(Team.BLUE_TEAM, Role.SLAYER, 1);
                }
            });
            grid.add(slayerSelect[1][1], 4, 1, 1, 1);
        }

        if (numOnTeam >= 3) {
            ImageView imRedSl = new ImageView(Images.RED_SLAYER_SELECT);
            imRedSl.setFitWidth(80);
            imRedSl.setFitHeight(80);

            grid.add(imRedSl, 0, 2, 1, 1);

            slayerSelect[0][2] = new Button("RED SLAYER: SELECT");
            slayerSelect[0][2].setPrefSize(200, 50);
            slayerSelect[0][2].setStyle(CssSheet.RED_SELECT_BUTTON);
            slayerSelect[0][2].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    lobbyClient.selectRole(Team.RED_TEAM, Role.SLAYER, 2);
                }
            });
            grid.add(slayerSelect[0][2], 1, 2, 1, 1);

            ImageView imBlueSl = new ImageView(Images.BLUE_SLAYER_SELECT);
            imBlueSl.setFitWidth(80);
            imBlueSl.setFitHeight(80);
            grid.add(imBlueSl, 3, 2, 1, 1);

            slayerSelect[1][2] = new Button("BLUE SLAYER: SELECT");
            slayerSelect[1][2].setPrefSize(200, 50);
            slayerSelect[1][2].setStyle(CssSheet.BLUE_SELECT_BUTTON);
            slayerSelect[1][2].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    lobbyClient.selectRole(Team.BLUE_TEAM, Role.SLAYER, 2);
                }
            });
            grid.add(slayerSelect[1][2], 4, 2, 1, 1);
        }

        if (numOnTeam >= 4) {
            ImageView imRedSl = new ImageView(Images.RED_SLAYER_SELECT);
            imRedSl.setFitWidth(80);
            imRedSl.setFitHeight(80);

            grid.add(imRedSl, 0, 3, 1, 1);

            slayerSelect[0][3] = new Button("RED SLAYER: SELECT");
            slayerSelect[0][3].setPrefSize(200, 50);
            slayerSelect[0][3].setStyle(CssSheet.RED_SELECT_BUTTON);
            slayerSelect[0][3].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    lobbyClient.selectRole(Team.RED_TEAM, Role.SLAYER, 3);
                }
            });
            grid.add(slayerSelect[0][3], 1, 3, 1, 1);

            ImageView imBlueSl = new ImageView(Images.BLUE_SLAYER_SELECT);
            imBlueSl.setFitWidth(80);
            imBlueSl.setFitHeight(80);
            grid.add(imBlueSl, 3, 3, 1, 1);

            slayerSelect[1][3] = new Button("BLUE SLAYER: SELECT");
            slayerSelect[1][3].setPrefSize(200, 50);
            slayerSelect[1][3].setStyle(CssSheet.BLUE_SELECT_BUTTON);
            slayerSelect[1][3].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    lobbyClient.selectRole(Team.BLUE_TEAM, Role.SLAYER, 3);
                }
            });
            grid.add(slayerSelect[1][3], 4, 3, 1, 1);
        }

        ready = new Button("Ready");
        ready.setPrefSize(200, 30);
        ready.setFont(Font.font(20));
        ready.setStyle(CssSheet.YELLO_BUTTON_CSS);
//        GridPane.setConstraints(ready, 3, 0);
        grid.add(ready, 2, 5, 1, 1);



        ready.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Log.info("client click ready");
                ready();
                //the following would be done in the network part

//                window.setScene(new Scene(choiceTeamAndRoleScene()));
            }

        });

        InetAddress serverAddr = null;
        try {
            serverAddr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (lobbyServer != null) {
            Text addrText = new Text("IP: "+ serverAddr.toString().split("/")[1]);
            addrText.setFont(Font.font("", FontWeight.BOLD, 25));
            addrText.setFill(Color.web("#090a0c"));
            grid.add(addrText, 2, 6, 3, 1);
        }

        return grid;
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



    private GridPane ipForm() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(window.getHeight()/2, 200, window.getHeight()/2 + 100, 500));
        grid.setVgap(15);
        grid.setHgap(5);

        playerName = new TextField();
        playerName.setPrefSize(600, 80);
        playerName.setPromptText("Enter your name.");
        playerName.setText("Default Name");
        playerName.setPrefColumnCount(50);

        playerName.setFont(Font.font("Verdana",30));
        grid.getChildren().add(playerName);

        //Defining the Name text field
        final TextField ip = new TextField();

        ContextMenu hostsDropDown = new ContextMenu();
        hostsDropDown.hide();

        hostDropDownItem.set(new CustomMenuItem());

        Text itemText = new Text("No host found by host discovery");
        itemText.setFont(Font.font("", FontWeight.BOLD, 25));
        hostDropDownItem.get().setContent(itemText);

        hostDropDownText.set(itemText);


        hostsDropDown.getItems().add(hostDropDownItem.get());
        hostDropDownItem.get().setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                ip.setText(((Text)hostDropDownItem.get().getContent()).getText());
            }
        });

        ip.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        addr[0] = lobbyClient.discoverHost();
//                    }
//                }).start();
//                if (addr[0] == null) {
//                    ((Text)tmp.getContent()).setText("No host found yet");
//                } else {
//                    ((Text)tmp.getContent()).setText(addr[0].toString());
//                }

//                System.out.println("focued listener: " + addr[0]);
//                hosts.hide();


                hostsDropDown.show(ip, Side.BOTTOM, 0, 0);
            }
        });

        ip.setPrefSize(600, 80);
        ip.setPromptText("Enter ip.");
        ip.setPrefColumnCount(100);

        ip.setFont(Font.font("Verdana",30));
//        ip.fontProperty().set(Font.font(20));

//        ip.setScaleX(10);
//        ip.setScaleY(5);

//        ip.getText();
        GridPane.setConstraints(ip, 0, 1);
        grid.getChildren().add(ip);

        Button connect = new Button("Connect");
        connect.fontProperty().set(Font.font(20));

        connect.setStyle(CssSheet.YELLO_BUTTON_CSS);

        connect.setPrefSize(200, 80);
        GridPane.setConstraints(connect, 0, 2);
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
}
