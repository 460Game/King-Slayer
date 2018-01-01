
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<KeyEvent> {
    final int WIDTH = 600;
    final int HEIGHT = 400;

    double ballRadius = 40;
    double ballX = 100;
    double ballY = 200;
    double xSpeed = 4;
    double ySpeed = 4;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Basic JavaFX demo");

        Group root = new Group();
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Bouncing Ball
        Circle circle = new Circle();
        circle.setCenterX(ballX);
        circle.setCenterY(ballY);
        circle.setRadius(ballRadius);
        circle.setFill(Color.BLUE);
        root.getChildren().add(circle);

        // need to attach KeyEvent caller to a Node of some sort.
        // How about an invisible Box?
        final Box keyboardNode = new Box();
        keyboardNode.setFocusTraversable(true);
        keyboardNode.requestFocus();
        keyboardNode.setOnKeyPressed(this);

        root.getChildren().add(keyboardNode);

        stage.setScene(scene);
        stage.show();

        AnimationTimer animator = new AnimationTimer() {

            @Override
            public void handle(long arg0) {

                // UPDATE
                ballX += xSpeed;
                ballY += ySpeed;

                if (ballX + ballRadius >= WIDTH) {
                    ballX = WIDTH - ballRadius;
                    xSpeed *= -1;
                } else if (ballX - ballRadius < 0) {
                    ballX = 0 + ballRadius;
                    xSpeed *= -1;
                }

                if (ballY + ballRadius >= HEIGHT) {
                    ballY = HEIGHT - ballRadius;
                    ySpeed *= -1;
                } else if (ballY - ballRadius < 0) {
                    ballY = 0 + ballRadius;
                    ySpeed *= -1;
                }


                // RENDER
                circle.setCenterX(ballX);
                circle.setCenterY(ballY);
            }
        };

        animator.start();
    }

    @Override
    public void handle(KeyEvent arg0) {

        if (arg0.getCode() == KeyCode.LEFT) {
            xSpeed = -4;
            ySpeed = 0;
        } else if (arg0.getCode() == KeyCode.RIGHT) {
            xSpeed = 4;
            ySpeed = 0;
        } else if (arg0.getCode() == KeyCode.UP) {
            xSpeed = 0;
            ySpeed = -4;
        } else if (arg0.getCode() == KeyCode.DOWN) {
            ySpeed = 4;
            xSpeed = 0;
        }
    }
}