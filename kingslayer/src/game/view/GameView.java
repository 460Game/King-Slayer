package game.view;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.ClientGameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.gameState.Loading;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lobby.Main;

import java.util.*;

import static images.Images.GAME_CURSOR_IMAGE;
import static util.Const.FPSPrint;
import static util.Util.toWorldCoords;

public class GameView {

    private ClientGameModel model;
    private Stage window;
    private Main mainApp;
    private AnimationTimer timer;

    public GameView(ClientGameModel model, Main mainApp) {
        this.model = model;
        this.mainApp = mainApp;
    }

    public void start(Stage window) {
        this.window = window;
        Group root = new Group();

        //TODO loading screen
        while (model.getState() == Loading.SINGLETON) {
            model.update();
        }

        WorldPanel worldPanel = new WorldPanel(model);
        GameInteractionLayer gameInteractionLayer = new GameInteractionLayer(model, worldPanel);
        Minimap minimap = new Minimap(model, worldPanel);
        InfoPanel infoPanel = new InfoPanel(model);
        ActionPanel actionPanel = new ActionPanel(model);
        ResourcePanel resourcePanel = new ResourcePanel(model);
        ExitPrompt exitPrompt = new ExitPrompt(model);
        TeamWinPrompt teamWinPrompt = new TeamWinPrompt(model, this);
        TeamLosePrompt teamLosePrompt = new TeamLosePrompt(model, this);

        gameInteractionLayer.prefWidthProperty().bind(window.widthProperty());
        gameInteractionLayer.prefHeightProperty().bind(window.heightProperty());
        minimap.prefWidthProperty().bind(window.heightProperty().multiply(0.35));
        minimap.prefHeightProperty().bind(window.heightProperty().multiply(0.35));
        minimap.layoutYProperty().bind(window.heightProperty().multiply(0.65));

        infoPanel.prefWidthProperty().bind(window.widthProperty().multiply(0.5));
        infoPanel.prefHeightProperty().bind(window.heightProperty().multiply(0.1));
        infoPanel.layoutXProperty().bind(window.widthProperty().multiply(0.5));
        infoPanel.layoutYProperty().bind(window.heightProperty().multiply(0.9));

        actionPanel.prefWidthProperty().bind(window.widthProperty().subtract(minimap.prefWidthProperty()).subtract(infoPanel.prefWidthProperty()));
        actionPanel.prefHeightProperty().bind(window.heightProperty().multiply(0.2));
        actionPanel.layoutXProperty().bind(minimap.widthProperty());
        actionPanel.layoutYProperty().bind(window.heightProperty().multiply(0.8));

        resourcePanel.prefWidthProperty().bind(window.widthProperty().multiply(0.2));
        resourcePanel.prefHeightProperty().bind(window.heightProperty().multiply(0.05));
        resourcePanel.layoutXProperty().bind(window.widthProperty().multiply(0.8));

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

        root.getChildren().addAll(gameInteractionLayer, minimap, infoPanel, actionPanel, resourcePanel,
                exitPrompt, teamLosePrompt, teamWinPrompt);


        //FPS print
        final int[] totalFrameCount = {0};
        TimerTask updateFPS = new TimerTask() {
            public void run() {
                Log.info(String.valueOf("ClientFPS: " + totalFrameCount[0]));
                totalFrameCount[0] = 0;
            }
        };

        if (FPSPrint) {
            Timer t = new Timer();
            t.scheduleAtFixedRate(updateFPS, 1000, 1000);
        }


        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (model.getWinningTeam() == null) {
                    gameInteractionLayer.setVisible(true);
                    resourcePanel.setVisible(true);
                    minimap.setVisible(true);
                    infoPanel.setVisible(true);
                    teamLosePrompt.setVisible(false);
                    actionPanel.setVisible(true);

                    totalFrameCount[0]++;

                    model.update();
                    gameInteractionLayer.draw();
                    resourcePanel.draw();
                    minimap.draw();
                    infoPanel.draw();
                    actionPanel.draw();

                    //TODO dont do this
                    resourcePanel.setBorder(new Border(new BorderStroke(model.getLocalPlayer().getTeam().color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
                    minimap.setBorder(new Border(new BorderStroke(model.getLocalPlayer().getTeam().color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
                    infoPanel.setBorder(new Border(new BorderStroke(model.getLocalPlayer().getTeam().color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
                    actionPanel.setBorder(new Border(new BorderStroke(model.getLocalPlayer().getTeam().color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));

                } else if (model.getWinningTeam() == model.getLocalPlayer().getTeam()) {
                    teamWinPrompt.setVisible(true);
                    gameInteractionLayer.setVisible(false);
                    resourcePanel.setVisible(false);
                    minimap.setVisible(false);
                    infoPanel.setVisible(false);
                    actionPanel.setVisible(false);
                } else {
                    gameInteractionLayer.setVisible(false);
                    resourcePanel.setVisible(false);
                    minimap.setVisible(false);
                    infoPanel.setVisible(false);
                    teamLosePrompt.setVisible(true);
                    actionPanel.setVisible(false);
                }

            }
        };

        timer.start();

        Scene scene = new Scene(root);

        scene.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
        scene.setOnScroll(e -> {

        });

        scene.setOnKeyPressed(e -> {
            KeyCode kc = e.getCode();


            if (kc == KeyCode.ESCAPE)
                exitPrompt.setVisible(!exitPrompt.isVisible());
        });


        window.setScene(scene);
    }

    public void goBackToMain() {
        mainApp.closeServer();
        window.setScene(mainApp.mainMenuScene);
        mainApp.start(window);
        timer.stop();

        timer = null;
        model = null;
        mainApp = null;
        window = null;
    }

    public void restart() {
        mainApp.restart(window);
        timer.stop();

        timer = null;
        model = null;
        mainApp = null;
        window = null;
    }
}
