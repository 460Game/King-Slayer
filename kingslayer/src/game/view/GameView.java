package game.view;

import game.message.toServer.GoDirectionRequest;
import game.message.toServer.StopRequest;
import game.model.game.model.ClientGameModel;
import game.model.game.model.team.Team;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import lobby.Main;

import java.util.*;

import static images.Images.GAME_CURSOR_IMAGE;
import static util.Util.toWorldCoords;

public class GameView {

    private ClientGameModel model;
    Stage window;

    public GameView(ClientGameModel model) {
        this.model = model;
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
        TeamWinPrompt teamWinPrompt = new TeamWinPrompt(model);
        TeamLosePrompt teamLosePrompt = new TeamLosePrompt(model);

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
        AnimationTimer timer = null;
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (model.getWinningTeam() == Team.NEUTRAL) {
                    //nop
                }
                else if (model.getWinningTeam() == model.getLocalPlayer().team) {
                    teamWinPrompt.setVisible(true);
                }

                else {
                    teamLosePrompt.setVisible(true);
                }

                resourcePanel.updateResources();
                minimap.draw();
                worldPanel.draw();
                infoPanel.update();
            }
        };

        timer.start();

        Scene scene = new Scene(root);

        scene.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
        scene.setOnScroll(e -> {

        });


        int[] dir = {0,0};

        Set<KeyCode> currentlyPressed = new TreeSet<>();

//        Set<GridCell> nextDestination = astar.getPassable();
//        Iterator<GridCell> it = nextDestination.iterator();

        scene.setOnKeyPressed(e -> {
            if(currentlyPressed.contains(e.getCode()))
                return;
            currentlyPressed.add(e.getCode());

            if (e.getCode() == KeyCode.F11) window.setFullScreen(true);
            if (e.getCode() == KeyCode.W) // Start upward movement.
                dir[1]--;
            if (e.getCode() == KeyCode.S) // Start downward movement.
                dir[1]++;
            if (e.getCode() == KeyCode.A) // Start leftward movement.
                dir[0]--;
            if (e.getCode() == KeyCode.D) // Start rightward movement.
                dir[0]++;

            if (e.getCode() == KeyCode.ESCAPE)
                exitPrompt.setVisible(true);


            if(dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopRequest(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionRequest(model.getLocalPlayer().id, Math.atan2(dir[1],dir[0])));
        });

        scene.setOnKeyReleased(e -> {
            currentlyPressed.remove(e.getCode());
            if (e.getCode() == KeyCode.W) // Stop upward movement.
                dir[1]++;
            if (e.getCode() == KeyCode.S) // stop downward movement.
                dir[1]--;
            if (e.getCode() == KeyCode.A) // stop leftward movement.
                dir[0]++;
            if (e.getCode() == KeyCode.D) // stop rightward movement.
                dir[0]--;

            if(dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopRequest(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionRequest(model.getLocalPlayer().id, Math.atan2(dir[1],dir[0])));
        });

        window.setScene(scene);

        window.setFullScreen(true);
    }

    public void goBackToMain() {
//        window.setScene(Main.mainMenuScene);
    }
}
