package game.view;

import game.ai.Astar;
import game.message.toClient.NewEntityMessage;
import game.message.toServer.GoDirectionMessage;
import game.message.toServer.StopMessage;
import game.model.game.grid.GridCell;
import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Visitor;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.*;

import static images.Images.GAME_CURSOR_IMAGE;
import static util.Util.toWorldCoords;

public class GameView {

    private ClientGameModel model;

//    private Astar astar = new Astar(model);

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
            double xdiff = e.getX() - worldPanel.getWidth() / 2;
            double ydiff = e.getY() - worldPanel.getHeight() / 2;
            double xCoords = toWorldCoords(xdiff);
            double yCoords = toWorldCoords(ydiff);
            double actualx = model.getLocalPlayer().data.x + xCoords;
            double actualy = model.getLocalPlayer().data.y + yCoords;
            model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, Math.atan2(actualy -
                    model.getLocalPlayer().data.y, actualx - model.getLocalPlayer().data.x)));
        });

        scene.setOnMouseMoved(e -> {

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

//            if (e.getCode() == KeyCode.SPACE) {
//                GridCell end = astar.getPassable().iterator().next();
////                int startx = (int) model.getLocalPlayer().data.x;
////                int starty = (int) model.getLocalPlayer().data.y;
////                System.out.println("Start x, y: " + startx + ", " + starty);
////                System.out.println("End x, y: " + end.getTopLeftX() + ", " + end.getTopLeftY());
//                astar.findPath(model.getCell((int) model.getLocalPlayer().data.x, (int) model.getLocalPlayer().data.y),
//                        end);
//            }

//            if (e.getCode() == KeyCode.ENTER) {
//                GridCell end = it.next();
////                int startx = (int) model.getLocalPlayer().data.x;
////                int starty = (int) model.getLocalPlayer().data.y;
////                System.out.println("Start x, y: " + startx + ", " + starty);
////                System.out.println("End x, y: " + end.getTopLeftX() + ", " + end.getTopLeftY());
//                astar.findPath(model.getCell((int) model.getLocalPlayer().data.x, (int) model.getLocalPlayer().data.y),
//                        end);
//                it.remove();
//            }

            if (e.getCode() == KeyCode.DIGIT1 || e.getCode() == KeyCode.NUMPAD1 ||
                e.getCode() == KeyCode.DIGIT2 || e.getCode() == KeyCode.NUMPAD2)
                new Visitor.ShowPlacement().run(model.getLocalPlayer(), model);

            if(dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, Math.atan2(dir[1],dir[0])));
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

            if (e.getCode() == KeyCode.DIGIT1 || e.getCode() == KeyCode.NUMPAD1)
                new Visitor.PlaceWall().run(model.getLocalPlayer(), model);

            if (e.getCode() == KeyCode.DIGIT2 || e.getCode() == KeyCode.NUMPAD2)
                new Visitor.PlaceResourceCollector().run(model.getLocalPlayer(), model);

            if(dir[0] == 0 && dir[1] == 0)
                model.processMessage(new StopMessage(model.getLocalPlayer().id));
            else
                model.processMessage(new GoDirectionMessage(model.getLocalPlayer().id, Math.atan2(dir[1],dir[0])));
        });

        window.setScene(scene);

        window.setFullScreen(true);
    }
}
