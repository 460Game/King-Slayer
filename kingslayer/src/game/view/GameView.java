package game.view;

import game.message.toServer.GoDirectionMessage;
import game.message.toServer.StopMessage;
import game.model.game.model.ClientGameModel;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import static images.Images.GAME_CURSOR_IMAGE;
import static util.Const.*;
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

        root.getChildren().addAll(worldPanel, minimap, infoPanel, actionPanel, resourcePanel);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                resourcePanel.updateResources();
                minimap.draw();
                worldPanel.draw();
            }
        };

        timer.start();

        Scene scene = new Scene(root);

        scene.setCursor(new ImageCursor(GAME_CURSOR_IMAGE, GAME_CURSOR_IMAGE.getWidth() / 2, GAME_CURSOR_IMAGE.getHeight() / 2));
        scene.setOnScroll(e -> {

        });

        scene.setOnMouseClicked(e -> {

        });

        scene.setOnMouseMoved(e -> {

        });

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) window.setFullScreen(true);
            if (e.getCode() == KeyCode.W) // Start upward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_UP));
            if (e.getCode() == KeyCode.S) // Start downward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_DOWN));
            if (e.getCode() == KeyCode.A) // Start leftward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_LEFT));
            if (e.getCode() == KeyCode.D) // Start rightward movement.
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, ANGLE_RIGHT));
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.W) // Stop upward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
            if (e.getCode() == KeyCode.S) // Stop downward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
            if (e.getCode() == KeyCode.A) // Stop leftward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
            if (e.getCode() == KeyCode.D) // Stop rightward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
        });

        window.setScene(scene);
    }
}
