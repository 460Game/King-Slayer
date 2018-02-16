package game.view;

import game.message.toServer.GoDirectionRequest;
import game.message.toServer.StopRequest;
import game.model.game.model.ClientGameModel;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.*;

import static images.Images.GAME_CURSOR_IMAGE;
import static javafx.scene.input.KeyCode.*;
import static util.Util.toWorldCoords;

public class GameView {

    private ClientGameModel model;

    public GameView(ClientGameModel model) {
        this.model = model;
    }

    public void start(Stage window) {
        Group root = new Group();

        Minimap minimap = new Minimap(model);
        WorldPanel worldPanel = new WorldPanel(model);
        InfoPanel infoPanel = new InfoPanel(model);
        ActionPanel actionPanel = new ActionPanel(model);
        ResourcePanel resourcePanel = new ResourcePanel(model);
        ExitPrompt exitPrompt = new ExitPrompt(model);

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

        exitPrompt.setVisible(false);

        root.getChildren().addAll(worldPanel, minimap, infoPanel, actionPanel, resourcePanel, exitPrompt);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                resourcePanel.update();
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


        int[] dir = {0, 0};

        Set<KeyCode> currentlyPressed = new TreeSet<>();

        scene.setOnKeyPressed(e -> {
            KeyCode kc = e.getCode();

            if (currentlyPressed.contains(kc))
                return;
            currentlyPressed.add(kc);

            if (kc == F11)
                window.setFullScreen(true);
            else if ((kc == W || kc == UP) && dir[1] >= 0) // Start upward movement.
                dir[1]--;
            else if ((kc == S || kc == DOWN) && dir[1] <= 0) // Start downward movement.
                dir[1]++;
            else if ((kc == A || kc == LEFT) && dir[0] >= 0) // Start leftward movement.
                dir[0]--;
            else if ((kc == D || kc == RIGHT) && dir[0] <= 0) // Start rightward movement.
                dir[0]++;
            else if (kc == KeyCode.ESCAPE)
                exitPrompt.setVisible(true);


            if (dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopRequest(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionRequest(model.getLocalPlayer().id, Math.atan2(dir[1], dir[0])));
        });

        scene.setOnKeyReleased(e -> {
            KeyCode kc = e.getCode();

            currentlyPressed.remove(e.getCode());

            if ((kc == W || kc == UP) && dir[1] < 0) // Stop upward movement.
                dir[1]++;
            if ((kc == S || kc == DOWN) && dir[1] > 0) // stop downward movement.
                dir[1]--;
            if ((kc == A || kc == LEFT) && dir[0] < 0) // stop leftward movement.
                dir[0]++;
            if ((kc == D || kc == RIGHT) && dir[0] > 0) // stop rightward movement.
                dir[0]--;

            if (dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopRequest(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionRequest(model.getLocalPlayer().id, Math.atan2(dir[1], dir[0])));
        });

        window.setScene(scene);
        window.setFullScreen(true);
    }
}
