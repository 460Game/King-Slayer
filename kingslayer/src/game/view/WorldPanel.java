package game.view;

import game.model.game.model.ClientGameModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Region;
import javafx.scene.transform.Affine;

import static util.Const.*;
import static util.Util.toDrawCoords;
import static util.Util.toWorldCoords;

/**
 * the main node- contains all the world!
 */
public class WorldPanel extends Region {

    private WritableImage BGImage1;
    private WritableImage BGImage2;
    private ClientGameModel model;
    private Canvas fgCanvas;
    private Canvas bgCanvas;
    private GraphicsContext fgGC;
    private GraphicsContext bgGC;

    private GameInteractionLayer gameInteractionLayer;

    WorldPanel(ClientGameModel model) {
        this.model = model;
        fgCanvas = new Canvas();
        bgCanvas = new Canvas();
        gameInteractionLayer = new GameInteractionLayer(model);
        fgGC = fgCanvas.getGraphicsContext2D();
        bgGC = bgCanvas.getGraphicsContext2D();

        this.getChildren().addAll(bgCanvas, fgCanvas, gameInteractionLayer);

        fgCanvas.heightProperty().bind(this.heightProperty());
        fgCanvas.widthProperty().bind(this.widthProperty());
        bgCanvas.heightProperty().bind(this.heightProperty());
        bgCanvas.widthProperty().bind(this.widthProperty());
        gameInteractionLayer.prefHeightProperty().bind(this.heightProperty());
        gameInteractionLayer.prefWidthProperty().bind(this.widthProperty());

        //TODO this is sh*t
        new java.util.Timer().schedule(
            new java.util.TimerTask() {
                @Override
                public void run() {
                    BGImage1 = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
                    model.writeBackground(BGImage1,true);
                    BGImage2 = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
                    model.writeBackground(BGImage2,false);
                }
            },
            3000
        );

    }

    private double[] scaleFactor = {1};
    private int tick[] = {0};

    public void update() {
        model.update();

        if (model.getLocalPlayer() != null) {

            double x = model.getLocalPlayer().data.x;
            double y = model.getLocalPlayer().data.y;
            double gameW = toWorldCoords(getWidth() / scaleFactor[0]);
            double gameH = toWorldCoords(getHeight() / scaleFactor[0]);
            double xt = -toDrawCoords(x * scaleFactor[0]) + getWidth() / 2;
            double yt = -toDrawCoords(y * scaleFactor[0]) + getHeight() / 2;
            fgGC.setTransform(new Affine());
            bgGC.setTransform(new Affine());
            //       fgGC.transform(new Affine(Affine.translate(CANVAS_WIDTH/2, CANVAS_HEIGHT/2)));
            //                    fgGC.transform(new Affine(Affine.scale(scaleFactor[0], scaleFactor[0])));
            //                    fgGC.transform(new Affine(Affine.scale(scaleFactor[0], scaleFactor[0])));
            //      fgGC.transform(new Affine(Affine.translate(-CANVAS_WIDTH/2, -CANVAS_HEIGHT/2)));
            fgGC.translate(xt, yt);
            bgGC.translate(xt, yt);

            fgGC.clearRect(-1111, -11111, 11111111, 1111111);

            if(tick[0] > WATER_ANIM_PERIOD/2)
                bgGC.drawImage(BGImage1, 0, 0);
            else
                bgGC.drawImage(BGImage2, 0, 0);

            model.drawForeground(fgGC, GRID_X_SIZE / 2, GRID_Y_SIZE / 2, GRID_X_SIZE, GRID_Y_SIZE);

            tick[0] = (tick[0] + 1) % (WATER_ANIM_PERIOD);
        }
    }
}
