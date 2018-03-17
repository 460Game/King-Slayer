package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import util.Const;
import util.Util;

import static images.Images.CURSOR_IMAGE;

/**
 * panel in bottem left with the minimap
 */
public class Minimap extends Region {

    private WorldPanel world;
    private ClientGameModel model;
    private Canvas minimapCanvas;
    private GraphicsContext minimapGC;

    Minimap(ClientGameModel model, WorldPanel world) {
        this.model = model;
        this.world = world;
        minimapCanvas = new Canvas();
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        minimapGC = minimapCanvas.getGraphicsContext2D();

        this.getChildren().add(minimapCanvas);

        minimapCanvas.heightProperty().bind(this.heightProperty().subtract(50));
        minimapCanvas.widthProperty().bind(this.widthProperty().subtract(50));
        minimapCanvas.translateXProperty().setValue(25);
        minimapCanvas.translateYProperty().setValue(25);

        this.setOnMouseDragged(e -> {
            world.setMiniPos( -3+(e.getX())/minimapGC.getCanvas().getWidth() * (model.getMapWidth()-8),
                     -3+(e.getY())/minimapGC.getCanvas().getHeight() * (model.getMapHeight()-8));
        });

        this.setOnMousePressed(e -> {
            world.miniDown();
        });

        this.setOnMouseExited(e -> {
            world.miniUp();
        });

        this.setOnMouseReleased(e -> {
            world.miniUp();
        });
    }

    private int tick = 0;

    public void draw() {
        tick++;
        if (tick == 5) {
            tick = 0;
            minimapGC.setTransform(new Affine(Transform.scale(
                    minimapGC.getCanvas().getWidth() / (model.getMapWidth()-8),
                    minimapGC.getCanvas().getHeight() / (model.getMapHeight()-8))));
            minimapGC.clearRect(0, 0, minimapCanvas.getWidth(), minimapCanvas.getHeight());
            minimapGC.setGlobalAlpha(0.3);

            for (int x = 4; x < model.getMapWidth()-4; x++) {
                for (int y = 4; y < model.getMapHeight()-4; y++) {
                        if (model.getCell(x, y).isVisable(model.getTeam()))
                            minimapGC.setFill(model.getTile(x, y).getColor());
                        else if (model.getCell(x, y).isExplored(model.getTeam()))
                            minimapGC.setFill(model.getTile(x, y).getColor().interpolate(Color.BLACK, 0.7));
                        else
                            minimapGC.setFill(Color.BLACK);
                        minimapGC.fillRect(x - 3, y - 3, 2, 2);
                }
            }
            minimapGC.setGlobalAlpha(1);
            for (Entity player : model.getAllEntities()) {
                player.containedIn.stream().findAny().ifPresent(cell -> {
                    if (cell.isVisable(model.getTeam())) {
                        if (player.has(Entity.EntityProperty.ROLE)) {
                            minimapGC.setFill(player.getTeam().color);
                            minimapGC.fillOval(player.getX()-3, player.getY()-3, 3, 3);
                        } else if (player.has(Entity.EntityProperty.TEAM)) {
                            minimapGC.setFill(player.getTeam().color);
                            minimapGC.fillOval(player.getX()-3, player.getY()-3, 1, 1);
                        }
                    }
                });
            }
            minimapGC.setStroke(Color.WHITE);
            minimapGC.strokeRect(world.getCamX() - 2 - world.getCamW() / 2, world.getCamY() - 2 - world.getCamH() / 2, world.getCamW(), world.getCamH());
        }


    }
    public void stop() {
        model = null;
        world.stop();
        world = null;
    }
}

