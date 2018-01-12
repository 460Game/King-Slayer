import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.input.KeyCode;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class LocalTest extends Application {

    GameModel model = new GameModel() {

        @Override
        public GameMap getGameMap() {
            return new GameMap();
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

    final static int SCREEN_WIDTH = 1500;
    final static int SCREEN_HEIGHT = 1000;

    public void start(Stage primaryStage) {

        primaryStage.setResizable(false);
        primaryStage.setTitle("King Slayer");
       // primaryStage.setFullScreen(true);
        Group root = new Group();
        Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);

        GraphicsContext gc = canvas.getGraphicsContext2D();
   // gc.transform(new Affine(Transform.shear(0.3,0.3, SCREEN_WIDTH/2, 0)));

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

        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.W) {
                gc.transform(new Affine(Transform.translate(0,100)));
            }
            if(e.getCode() == KeyCode.S) {
                gc.transform(new Affine(Transform.translate(0,-100)));
            }
            if(e.getCode() == KeyCode.A) {
                gc.transform(new Affine(Transform.translate(100,0)));
            }
            if(e.getCode() == KeyCode.D) {
                gc.transform(new Affine(Transform.translate(-100,0)));
            }
            if(e.getCode() == KeyCode.E) {
                gc.transform(new Affine(Transform.scale(1.1,1.1, canvas.getWidth()/2, canvas.getHeight()/2)));
            }
            if(e.getCode() == KeyCode.Q) {
                gc.transform(new Affine(Transform.scale(0.9,0.9, canvas.getWidth()/2, canvas.getHeight()/2)));
            }
            if(e.getCode() == KeyCode.SPACE) {
                model.gameMap = new GameMap();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();

   //     model.receiveUpdateCommand(new UpdateCommand(new Player(new CirlceShape(50,50,10), model)));

        //  serverModel.sendsInit();

        animator.start();
    }
}
