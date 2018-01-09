import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.stage.Stage;

public class LocalTest extends Application {

    GameModel serverModel = new GameModel() {

        Map map = new Map();

        @Override
        public Map getGameMap() {
            return map;
        }

        @Override
        WorldClock getTimer() {
            return new WorldClock() {
                @Override
                public long nanoTime() {
                    return System.nanoTime();
                }
            };
        }
    };

    GameModel clientModel = new GameModel() {

        @Override
        public Map getGameMap() {
            return serverModel.getGameMap();
        }

        @Override
        WorldClock getTimer() {
            return new WorldClock() {
                @Override
                public long nanoTime() {
                    return System.nanoTime();
                }
            };
        }
    };

    /**
     * starts the game window
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {

        serverModel.addUpdateListeners(clientModel);
        clientModel.addActionListeners(serverModel);


        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(1000, 1000);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer animator = new AnimationTimer()
        {
            @Override
            public void handle(long arg0)
            {
                // update
                serverModel.update();
                clientModel.update();

                clientModel.draw(gc);

            }
        };
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        //  serverModel.sendsInit();

        animator.start();
    }
}
