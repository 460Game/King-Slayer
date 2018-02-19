package game.view;

import game.ai.Astar;
import game.message.toServer.GoDirectionRequest;
import game.message.toServer.StopRequest;
import game.model.game.grid.GridCell;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Team;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lobby.Main;

import java.util.*;

import static images.Images.GAME_CURSOR_IMAGE;
import static javafx.scene.input.KeyCode.*;
import static util.Util.toWorldCoords;

public class GameView {

    private ClientGameModel model;
    Stage window;
    Main mainApp;
    AnimationTimer timer;

    public GameView(ClientGameModel model, Main mainApp) {
        this.model = model;
        this.mainApp = mainApp;
    }

    public void start(Stage window) {
        this.window = window;
        Group root = new Group();

        Minimap minimap = new Minimap(model);
        WorldPanel worldPanel = new WorldPanel(model);
        InfoPanel infoPanel = new InfoPanel(model);
        ActionPanel actionPanel = new ActionPanel(model);
        ResourcePanel resourcePanel = new ResourcePanel(model);
        ExitPrompt exitPrompt = new ExitPrompt(model);
        TeamWinPrompt teamWinPrompt = new TeamWinPrompt(model, this);
        TeamLosePrompt teamLosePrompt = new TeamLosePrompt(model, this);

        worldPanel.prefWidthProperty().bind(window.widthProperty());
        worldPanel.prefHeightProperty().bind(window.heightProperty());
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

        root.getChildren().addAll(worldPanel, minimap, infoPanel, actionPanel, resourcePanel,
            exitPrompt, teamLosePrompt, teamWinPrompt);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (model.getWinningTeam() == Team.NEUTRAL) {
                    model.update();
                    if (model.getLocalPlayer() != null) {
                        worldPanel.update();
                        resourcePanel.update();
                        minimap.draw();
                        infoPanel.update();
                        actionPanel.update();

                        //TODO dont do this
                        resourcePanel.setBorder(new Border(new BorderStroke(model.getLocalPlayer().team.color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
                        minimap.setBorder(new Border(new BorderStroke(model.getLocalPlayer().team.color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
                        infoPanel.setBorder(new Border(new BorderStroke(model.getLocalPlayer().team.color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
                        actionPanel.setBorder(new Border(new BorderStroke(model.getLocalPlayer().team.color, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));

                    }
                } else if (model.getWinningTeam() == model.getLocalPlayer().team) {
                    teamWinPrompt.setVisible(true);
                    worldPanel.setVisible(false);
                    resourcePanel.setVisible(false);
                    minimap.setVisible(false);
                    infoPanel.setVisible(false);
                    actionPanel.setVisible(false);
                } else {
                    worldPanel.setVisible(false);
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

        int[] dir = {0, 0};

        Set<KeyCode> currentlyPressed = new TreeSet<>();

        scene.setOnKeyPressed(e -> {
            KeyCode kc = e.getCode();

            if (currentlyPressed.contains(kc))
                return;
            currentlyPressed.add(kc);

            if (kc == W || kc == UP) // Start upward movement.
                dir[1]--;
            else if (kc == S || kc == DOWN) // Start downward movement.
                dir[1]++;
            else if (kc == A || kc == LEFT) // Start leftward movement.
                dir[0]--;
            else if (kc == D || kc == RIGHT) // Start rightward movement.
                dir[0]++;
            else if (kc == KeyCode.ESCAPE)
                exitPrompt.setVisible(!exitPrompt.isVisible());


            if (dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopRequest(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionRequest(model.getLocalPlayer().id, Math.atan2(dir[1], dir[0])));
        });

        scene.setOnKeyReleased(e -> {
            KeyCode kc = e.getCode();

            currentlyPressed.remove(e.getCode());

            if (kc == W || kc == UP) // Stop upward movement.
                dir[1]++;
            if (kc == S || kc == DOWN) // stop downward movement.
                dir[1]--;
            if (kc == A || kc == LEFT) // stop leftward movement.
                dir[0]++;
            if (kc == D || kc == RIGHT) // stop rightward movement.
                dir[0]--;

            if (dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopRequest(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionRequest(model.getLocalPlayer().id, Math.atan2(dir[1], dir[0])));
        });

        window.setScene(scene);
        window.setFullScreen(true);
    }

    public void goBackToMain() {
        mainApp.closeServer();
//        timer.stop();
//        Main newMain = new Main();
//        newMain.start(window);
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
