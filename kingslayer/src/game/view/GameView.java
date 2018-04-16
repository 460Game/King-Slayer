package game.view;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.ClientGameModel;
import game.model.game.model.gameState.Loading;
import game.model.game.model.team.Role;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lobby.GameView2MainAdaptor;
import music.MusicPlayer;

import java.util.*;

import static images.Images.GAME_CURSOR_IMAGE;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F11;
import static javafx.scene.input.KeyCode.TAB;
import static util.Const.FPSPrint;
import static util.Util.toWorldCoords;

public class GameView {

    private ClientGameModel model;
    private Stage window;
    private GameView2MainAdaptor mainApp;
    private AnimationTimer timer;

    private long startTime;
    private double elapsedTime;

    Group root;
    WorldPanel worldPanel;
    GameInteractionLayer gameInteractionLayer;
    int actionPanelYOffset;
    ActionPanel actionPanel;
    Minimap minimap;
    ResourcePanel resourcePanel;
    ExitPrompt exitPrompt;
    TeamWinPrompt teamWinPrompt;
    TeamLosePrompt teamLosePrompt;

    public void stop() {
        model = null;
        window = null;
        mainApp = null;
        if (timer != null)
            timer.stop();
        timer = null;
    }

    public GameView(ClientGameModel model, GameView2MainAdaptor mainApp) {
        this.model = model;
        this.mainApp = mainApp;
        System.out.println("make game view " + this);
    }

    public void start(Stage window) {
        this.window = window;
        root = new Group();

        //TODO loading screen
        while (model.getState() == Loading.SINGLETON) {
            model.update();
        }

        worldPanel = new WorldPanel(model);

        if (model.getLocalPlayer().getRole() == Role.KING) {
            gameInteractionLayer = new KingGameInteractionLayer(model, worldPanel);
            actionPanel = new KingActionPanel(model, (KingGameInteractionLayer) gameInteractionLayer);
            actionPanelYOffset = 135;
        } else {
            gameInteractionLayer = new SlayerGameInteractionLayer(model, worldPanel);
            actionPanel = new SlayerActionPanel(model, (SlayerGameInteractionLayer) gameInteractionLayer);
            actionPanelYOffset = 165;
        }
        minimap = new Minimap(model, worldPanel);
        resourcePanel = new ResourcePanel(model);
        exitPrompt = new ExitPrompt(model, this);
        teamWinPrompt = new TeamWinPrompt(model, this);
        teamLosePrompt = new TeamLosePrompt(model, this);

        gameInteractionLayer.prefWidthProperty().bind(window.widthProperty());
        gameInteractionLayer.prefHeightProperty().bind(window.heightProperty());
        minimap.prefWidthProperty().bind(window.heightProperty().multiply(0.35));
        minimap.prefHeightProperty().bind(window.heightProperty().multiply(0.35));
        minimap.layoutYProperty().bind(window.heightProperty().multiply(0.65));

        actionPanel.setPrefWidth(335);
        actionPanel.setPrefHeight(130);
        actionPanel.layoutXProperty().bind(Bindings.max(minimap.widthProperty(), window.widthProperty().divide(2).subtract(150)));
        actionPanel.layoutYProperty().bind(window.heightProperty().subtract(actionPanelYOffset));

        // resourcePanel.setPrefWidth(300);
        //  resourcePanel.setPrefHeight(40);
        resourcePanel.layoutXProperty().bind(window.widthProperty().subtract(resourcePanel.widthProperty()));

        exitPrompt.prefHeightProperty().bind(window.heightProperty().multiply(0.3));
        exitPrompt.prefWidthProperty().bind(window.widthProperty().multiply(0.3));
        exitPrompt.layoutXProperty().bind(window.widthProperty().multiply(0.35));
        exitPrompt.layoutYProperty().bind(window.heightProperty().multiply(0.35));

        teamWinPrompt.prefHeightProperty().bind(window.heightProperty().multiply(0.3));
        teamWinPrompt.prefWidthProperty().bind(window.widthProperty().multiply(0.3));
        teamWinPrompt.layoutXProperty().bind(window.widthProperty().multiply(0.35));
        teamWinPrompt.layoutYProperty().bind(window.heightProperty().multiply(0.35));

        teamLosePrompt.prefHeightProperty().bind(window.heightProperty().multiply(0.3));
        teamLosePrompt.prefWidthProperty().bind(window.widthProperty().multiply(0.3));
        teamLosePrompt.layoutXProperty().bind(window.widthProperty().multiply(0.35));
        teamLosePrompt.layoutYProperty().bind(window.heightProperty().multiply(0.35));

        exitPrompt.setVisible(false);
        teamLosePrompt.setVisible(false);
        teamWinPrompt.setVisible(false);

        root.getChildren().addAll(gameInteractionLayer, minimap, actionPanel, resourcePanel,
                exitPrompt, teamLosePrompt, teamWinPrompt);


        final int[] totalFrameCount = {0};

        if (FPSPrint) {

            FPSPanel fpsPanel = new FPSPanel(model);
            fpsPanel.setPrefHeight(50);
            fpsPanel.setPrefWidth(120);
            root.getChildren().add(fpsPanel);

            //FPS print
            TimerTask updateFPS = new TimerTask() {
                public void run() {
                    long current = System.nanoTime();
                    elapsedTime = (double) (current - startTime);
                    fpsPanel.setFPS(totalFrameCount[0], elapsedTime / 1000000000.0);
                    totalFrameCount[0] = 0;
                }
            };


            Timer t = new Timer();
            t.scheduleAtFixedRate(updateFPS, 1000, 1000);
        }

        timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
//                System.out.println(GameView.this + " timer running ");
                try {

                    gameInteractionLayer.setVisible(true);
                    resourcePanel.setVisible(true);
                    minimap.setVisible(true);
                    teamLosePrompt.setVisible(false);
                    actionPanel.setVisible(true);

                    totalFrameCount[0]++;

                    if (model.getWinningTeam() == null) {
                        model.update();
                    }

                    gameInteractionLayer.draw();
                    resourcePanel.draw();
                    minimap.draw();
                    actionPanel.draw();

                    if (model.getWinningTeam() != null && model.getWinningTeam() == model.getTeam()) {
                        System.out.println("team win set visible");
                        teamWinPrompt.setVisible(true);
                    } else if (model.getWinningTeam() != null && model.getWinningTeam() != model.getTeam()) {
                        System.out.println("team lose set visible");
                        teamLosePrompt.setVisible(true);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
//                    Log.error("Game Loop Exception", e);
                }
            }
        };

        timer.start();

        startTime = System.nanoTime();

        Scene scene = new Scene(root);

        scene.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
        scene.setOnScroll(e -> {

        });

        scene.setOnKeyPressed(e -> {
            KeyCode kc = e.getCode();

            if (kc == KeyCode.ESCAPE || kc == ENTER)
                exitPrompt.setVisible(!exitPrompt.isVisible());

            if (kc == F11) {
                window.setFullScreen(!window.isFullScreen());
            }

            if (kc == TAB) {
                gameInteractionLayer.world.requestFocus();
                System.out.println("in GameView");
            }
        });


        window.setScene(scene);
    }

    public void goBackToMain() {
        MusicPlayer.playIntroMusic();
//        Platform.setImplicitExit(false);

        if (this.timer != null)
            this.timer.stop();

        System.out.println("check mainApp " +mainApp);
        int closeStatus = mainApp.closeServer();



//        figure out this
        mainApp.restartFromMainMenu();
        //figure out this

        this.timer = null;
        this.model = null;
        this.mainApp = null;
        this.window = null;
    }

    public void rematch() {
        if (this.timer != null)
            this.timer.stop();

//        Platform.setImplicitExit(false);

        Log.info("Rmatch, set everything to null");
        this.timer = null;
        if (this.model != null)
            this.model.stop();

        this.model = null;
        this.window = null;

        root = null;

        worldPanel.stop();
        worldPanel = null;

        gameInteractionLayer.stop();
        gameInteractionLayer = null;

        actionPanel.stop();
        actionPanel = null;

        minimap.stop();
        minimap = null;

        resourcePanel.stop();
        resourcePanel = null;

        exitPrompt.stop();
        exitPrompt = null;

//        teamWinPrompt.stop();
//        teamWinPrompt = null;

//        teamLosePrompt.stop();
//        teamLosePrompt = null;

        System.out.println("window is null " + window + " " + model);
        int status = mainApp.rematch();

//        mainApp = null;

//        try {
//            finalize();
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
    }

    //not used anymore
    public void restart() {

//        timer.stop();
//
//
//
//        Platform.setImplicitExit(false);
//
//        int status = mainApp.restart(window);
//        Log.info("Main restarts using window!");
//        timer = null;
//        model = null;
//        mainApp = null;
//        window = null;

    }
}
