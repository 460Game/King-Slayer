package game.view;

import game.model.game.model.ClientGameModel;
import game.model.game.model.GameModel;
import game.model.game.model.worldObject.entity.Entity;
import javafx.scene.ImageCursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

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

        minimapCanvas.heightProperty().bind(this.heightProperty().subtract(20));
        minimapCanvas.widthProperty().bind(this.widthProperty().subtract(20));
        minimapCanvas.translateXProperty().setValue(10);
        minimapCanvas.translateYProperty().setValue(10);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
    }

    private int tick = 0;

    public void draw() {

        tick++;
        if(tick == 5) {
            tick = 0;
            minimapGC.setTransform(new Affine(Transform.scale(
                minimapGC.getCanvas().getWidth() / model.getMapWidth(),
                minimapGC.getCanvas().getHeight() / model.getMapHeight())));
            minimapGC.fillRect(0, 0, minimapCanvas.getWidth(), minimapCanvas.getHeight());

            for (int x = 0; x < model.getMapWidth(); x++) {
                for (int y = 0; y < model.getMapHeight(); y++) {
                 //   if(model.getCell(x,y).streamContents().filter(e -> e.) != 0)
                //        minimapGC.setFill(Color.BLACK);
                //    else
                    if(model.getCell(x,y).isVisable(model.getLocalPlayer().getTeam()))
                        minimapGC.setFill(model.getTile(x, y).getColor());
                    else
                        minimapGC.setFill(Color.BLACK);
                    minimapGC.fillRect(x, y, 2, 2);
                }
            }
            //TEMP HACK
            for (Entity player : model.getAllEntities()) {
                if (player.has(Entity.EntityProperty.ROLE)) {
                    minimapGC.setFill(player.getTeam().color);
                    minimapGC.fillOval(player.getX(), player.getY(), 3, 3);
                } else if(player.has(Entity.EntityProperty.TEAM)){
                    minimapGC.setFill(player.getTeam().color);
                    minimapGC.fillOval(player.getX(), player.getY(), 1, 1);
                }
            }
            minimapGC.strokeRect(world.getCamX() - world.getCamW()/2, world.getCamY() - world.getCamH()/2, world.getCamW(), world.getCamH());
        }
    }
}
