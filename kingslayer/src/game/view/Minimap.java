package game.view;

import game.model.game.model.GameModel;
import game.model.game.model.team.Team;
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

    private GameModel model;
    private Canvas minimapCanvas;
    private GraphicsContext minimapGC;

    Minimap(GameModel model) {
        this.model = model;
        minimapCanvas = new Canvas();
        this.setCursor(new ImageCursor(CURSOR_IMAGE, 0, 0));
        minimapGC = minimapCanvas.getGraphicsContext2D();

        this.getChildren().add(minimapCanvas);

        minimapCanvas.heightProperty().bind(this.heightProperty().subtract(20));
        minimapCanvas.widthProperty().bind(this.widthProperty().subtract(20));
        minimapCanvas.translateXProperty().setValue(10);
        minimapCanvas.translateYProperty().setValue(10);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(10))));
     //   minimapCanvas.setHeight(100);
     //   minimapCanvas.setWidth(100);
      //  this.setHeight(100);
       // this.setWidth(100);
    }

    public void draw() {
       minimapGC.setTransform(new Affine(Transform.scale(
            minimapGC.getCanvas().getWidth() / model.getMapWidth(),
            minimapGC.getCanvas().getHeight() / model.getMapHeight())));
        minimapGC.fillRect(0, 0, minimapCanvas.getWidth(), minimapCanvas.getHeight());

        for (int x = 0; x < model.getMapWidth(); x++) {
            for (int y = 0; y < model.getMapHeight(); y++) {
                minimapGC.setFill(model.getTile(x, y).getColor());
                minimapGC.fillRect(x, y, 2, 2);
            }
        }
        //TEMP HACK
        for (Entity player : model.getAllEntities()) {
            if (player.team != Team.NEUTRAL) {
                minimapGC.setFill(player.team.color);
                minimapGC.fillOval(player.data.x, player.data.y, 3, 3);
            }
        }
    }
}
