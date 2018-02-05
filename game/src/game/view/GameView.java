package game.view;

import static Util.Const.*;

import game.Pathing.Astar;
import game.message.MoveMessage;
import game.message.StopMessage;
import game.model.Game.Grid.GridCell;
import game.model.Game.Model.ClientGameModel;
import game.model.Game.WorldObject.Entity.Entity;
import game.model.Game.WorldObject.Entity.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class GameView {

    private ClientGameModel model;

    private Astar astar;

    public GameView(ClientGameModel model) {
        this.model = model;
        astar = new Astar(model);
    }

    public void start(Stage window) {
        window.setResizable(true);
        window.setTitle("King Slayer");

        Group root = new Group();
        Canvas canvas = new Canvas(INIT_SCREEN_WIDTH, INIT_SCREEN_HEIGHT);
        Canvas debugCanvas = new Canvas(INIT_SCREEN_WIDTH, INIT_SCREEN_HEIGHT);
        Canvas minimapCanvas = new Canvas(INIT_SCREEN_WIDTH/3, INIT_SCREEN_HEIGHT/3);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        GraphicsContext minimapGC = minimapCanvas.getGraphicsContext2D();
        GraphicsContext debugGC = debugCanvas.getGraphicsContext2D();

        window.widthProperty().addListener(l -> {
            minimapCanvas.setWidth(Math.min(window.getWidth()/3, window.getHeight()/3));
            minimapCanvas.setHeight(Math.min(window.getWidth()/3, window.getHeight()/3));
            canvas.setWidth(window.getWidth());
            debugCanvas.setWidth(window.getWidth());
        });

        window.heightProperty().addListener(l -> {
            minimapCanvas.setWidth(Math.min(window.getWidth()/3, window.getHeight()/3));
            minimapCanvas.setHeight(Math.min(window.getWidth()/3, window.getHeight()/3));
            canvas.setHeight(window.getHeight());
            debugCanvas.setHeight(window.getHeight());
        });

        root.getChildren().add(canvas);
        root.getChildren().add(debugCanvas);
        root.getChildren().add(minimapCanvas);

        Scene scene = new Scene(root);

        double[] scaleFactor = {5};

        AnimationTimer animator = new AnimationTimer() {
            @Override
            public void handle(long arg0) {
                model.update();


                minimapGC.setTransform(new Affine(Transform.scale(
                    minimapGC.getCanvas().getWidth()/model.getMapWidth(),
                    minimapGC.getCanvas().getHeight()/model.getMapHeight())));
                minimapGC.fillRect(0,0,minimapGC.getCanvas().getWidth(), minimapGC.getCanvas().getHeight());
                for(int x= 0; x < model.getMapWidth(); x++){
                    for(int y =0; y< model.getMapHeight();y++) {
                        minimapGC.setFill(model.getTile(x,y).getColor());
                        minimapGC.fillRect(x,y,2,2);
                    }
                }
                //TEMP HACK
                for(Entity player : model.getAllEntities()) {
                    if(player instanceof Player) {
                        minimapGC.setFill(player.getTeam().color);
                        minimapGC.fillOval(player.getX(),player.getY(),3,3);
                    }
                }
                minimapGC.setTransform(new Affine());

                double gameW = scaleFactor[0] * window.getWidth() / TILE_PIXELS;
                double gameH = scaleFactor[0] * window.getHeight() / TILE_PIXELS;
                double xt = - model.getLocalPlayer().getX() * TILE_PIXELS + window.getWidth() / 2;
                double yt = -model.getLocalPlayer().getY() * TILE_PIXELS + (window.getHeight() / 2);
                gc.setTransform(new Affine(Affine.translate(xt, yt)));
                debugGC.setTransform(new Affine());
                debugGC.clearRect(0, 0, debugCanvas.getWidth(), debugCanvas.getHeight());
                debugGC.setTransform(new Affine(Affine.translate(xt, yt)));
                model.draw(gc, model.getLocalPlayer().getX(), model.getLocalPlayer().getY(), gameW, gameH);

                astar.draw(debugGC);

                //TODO temp for testing, doing this every frame
//    astar.findTraversableNodes();
//    Set<GridCell> nodes = astar.getNodes();
//    GridCell end = nodes.iterator().next();
//    int startx = (int) model.getLocalPlayer().getX();
//    int starty = (int) model.getLocalPlayer().getY();
//    astar.astar(model.getCell((int) model.getLocalPlayer().getX(), (int) model.getLocalPlayer().getY()), end);
}
        };

       /*scene.setOnScroll(e -> {
            if (e.getDeltaY() < 0) {
                scaleFactor[0] *= 1.1;
                gc.transform(new Affine(Affine.scale(0.9, 0.9)));
            } else {
                scaleFactor[0] *= 0.9;
                gc.transform(new Affine(Affine.scale(1.1, 1.1)));
            }
        });*/

        astar.findTraversableNodes();
        Set<GridCell> nextDestination = astar.getNodes();
        Iterator<GridCell> it = nextDestination.iterator();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) window.setFullScreen(true);
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) // Start upward movement.
                model.processMessage(new MoveMessage(model.getLocalPlayer().getId(), "up"));
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) // Start downward movement.
                model.processMessage(new MoveMessage(model.getLocalPlayer().getId(), "down"));
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) // Start leftward movement.
                model.processMessage(new MoveMessage(model.getLocalPlayer().getId(), "left"));
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) // Start rightward movement.
                model.processMessage(new MoveMessage(model.getLocalPlayer().getId(), "right"));
            // TODO remove, temp for testing
            if (e.getCode() == KeyCode.SPACE) {
                astar.findTraversableNodes();
                Set<GridCell> nodes = astar.getNodes();
                GridCell end = nodes.iterator().next();
                int startx = (int) model.getLocalPlayer().getX();
                int starty = (int) model.getLocalPlayer().getY();
                System.out.println("Start x, y: " + startx + ", " + starty);
                System.out.println("End x, y: " + end.getX() + ", " + end.getY());
                astar.astar(model.getCell((int) model.getLocalPlayer().getX(), (int) model.getLocalPlayer().getY()), end);
            }
            if (e.getCode() == KeyCode.ENTER) {
                astar.findTraversableNodes();
                GridCell end = it.next();
                int startx = (int) model.getLocalPlayer().getX();
                int starty = (int) model.getLocalPlayer().getY();
                System.out.println("Start x, y: " + startx + ", " + starty);
                System.out.println("End x, y: " + end.getX() + ", " + end.getY());
                astar.astar(model.getCell((int) model.getLocalPlayer().getX(), (int) model.getLocalPlayer().getY()), end);
                it.remove();
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W) // Stop upward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().getId(), "up"));
            if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S) // Stop downward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().getId(), "down"));
            if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A) // Stop leftward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().getId(), "left"));
            if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D) // Stop rightward movement.
                model.processMessage(new StopMessage(model.getLocalPlayer().getId(), "right"));
        });

        window.setScene(scene);
        window.show();
        animator.start();
    }
}
