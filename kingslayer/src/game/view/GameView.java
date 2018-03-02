package game.view;

import com.esotericsoftware.minlog.Log;
import game.message.toServer.StopRequest;
import game.model.game.model.ClientGameModel;
import game.model.game.model.ServerGameModel;
import game.model.game.model.gameState.Loading;
import game.model.game.model.team.Role;
import game.model.game.model.worldObject.entity.Entity;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lobby.Main;

import java.util.*;

import static images.Images.GAME_CURSOR_IMAGE;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F11;
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
        GameInteractionLayer gameInteractionLayer;
        ActionPanel actionPanel;
        if (model.getLocalPlayer().getRole() == Role.KING) {
            gameInteractionLayer = new KingGameInteractionLayer(model, worldPanel);
            actionPanel = new KingActionPanel(model, (KingGameInteractionLayer) gameInteractionLayer);
        } else {
            gameInteractionLayer = new SlayerGameInteractionLayer(model, worldPanel);
            actionPanel = new SlayerActionPanel(model, (SlayerGameInteractionLayer) gameInteractionLayer);
        }
        Minimap minimap = new Minimap(model, worldPanel);
        ResourcePanel resourcePanel = new ResourcePanel(model);
        ExitPrompt exitPrompt = new ExitPrompt(model);
        TeamWinPrompt teamWinPrompt = new TeamWinPrompt(model, this);
        TeamLosePrompt teamLosePrompt = new TeamLosePrompt(model, this);

        gameInteractionLayer.prefWidthProperty().bind(window.widthProperty());
        gameInteractionLayer.prefHeightProperty().bind(window.heightProperty());
        minimap.prefWidthProperty().bind(window.heightProperty().multiply(0.35));
        minimap.prefHeightProperty().bind(window.heightProperty().multiply(0.35));
        minimap.layoutYProperty().bind(window.heightProperty().multiply(0.65));

        actionPanel.setPrefWidth(520);
        actionPanel.setPrefHeight(130);
        actionPanel.layoutXProperty().bind(Bindings.max(minimap.widthProperty(), window.widthProperty().divide(2).subtract(250)));
        actionPanel.layoutYProperty().bind(window.heightProperty().subtract(135));

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

            FPSPanel fpsPanel = new FPSPanel();
            fpsPanel.setPrefHeight(50);
            fpsPanel.setPrefWidth(50);
            root.getChildren().add(fpsPanel);

            //FPS print
            TimerTask updateFPS = new TimerTask() {
                public void run() {
                    fpsPanel.setFPS(totalFrameCount[0]);
                    totalFrameCount[0] = 0;
                }
            };


            Timer t = new Timer();
            t.scheduleAtFixedRate(updateFPS, 1000, 1000);
        }


        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (model.clientLoseControl) {
                    if (model.respawnCnt == 200) {
                        System.out.println(model.respawnCnt);
                        model.respawnSlayerRequest();
                    }
                    model.respawnCnt++;
                }

                gameInteractionLayer.setVisible(true);
                resourcePanel.setVisible(true);
                minimap.setVisible(true);
                teamLosePrompt.setVisible(false);
                actionPanel.setVisible(true);

                totalFrameCount[0]++;

                model.update();
                gameInteractionLayer.draw();
                resourcePanel.draw();
                minimap.draw();
                actionPanel.draw();

                if (model.getWinningTeam() != null && model.getWinningTeam() == model.getTeam()) {
                    teamWinPrompt.setVisible(true);
                } else if (model.getWinningTeam() != null && model.getWinningTeam() != model.getTeam()) {
                    teamLosePrompt.setVisible(true);
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

        timer.stop();

        timer = null;

        int status = mainApp.restart(window);

        model = null;
        mainApp = null;
        window = null;

    }
}
