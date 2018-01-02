import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.stage.Stage;

public class LocalTest extends Application {

    GameModel model = new GameModel() {
        @Override
        WorldClock getTimer() {
            return new WorldClock() {
                @Override
                public long nanoTime() {
                    return System.nanoTime();
                }
            };
        }

        public void draw(GraphicsContext gc) {
            for(WorldObject o : model.map.inBox(0,0,0,0)) {
                o.draw(gc, 0, 0);
            }
        }
    };

    /**
     * starts the game window
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Drawing Operations Test");
        Group root = new Group();
        Canvas canvas = new Canvas(300, 250);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        model.draw(gc);
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
