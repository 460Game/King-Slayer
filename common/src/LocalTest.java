import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.stage.Stage;

public class LocalTest extends Application {

    GameModel model = new GameModel() {

        @Override
        public Map getGameMap() {
            return new Map();
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

    final static int SCREEN_WIDTH = 1000;
    final static int SCREEN_HEIGHT = 1000;

    public void start(Stage primaryStage) {

        primaryStage.setResizable(false);
        primaryStage.setTitle("King Slayer");
      //  primaryStage.setFullScreen(true);
        Group root = new Group();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        AnimationTimer animator = new AnimationTimer()
        {
            @Override
            public void handle(long arg0)
            {
                // update
                model.update();

                model.draw(gc);
            }
        };
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

   //     model.receiveUpdateCommand(new UpdateCommand(new Player(new CirlceShape(50,50,10), model)));

        //  serverModel.sendsInit();

        animator.start();
    }
}
