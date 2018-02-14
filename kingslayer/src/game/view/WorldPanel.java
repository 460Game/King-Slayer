package game.view;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.ClientGameModel;
import game.model.game.model.worldObject.entity.Visitor;
import javafx.animation.AnimationTimer;
import javafx.beans.InvalidationListener;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

import static util.Const.GRID_X_SIZE;
import static util.Const.GRID_Y_SIZE;
import static util.Const.WATER_ANIM_PERIOD;
import static util.Util.toDrawCoords;
import static util.Util.toWorldCoords;

/**
 * the main node- contains all the world!
 */
public class WorldPanel extends Region {

    private ClientGameModel model;
    private Canvas fgCanvas;
    private Canvas bgCanvas;
    private Canvas uiCanvas;
    private GraphicsContext fgGC;
    private GraphicsContext bgGC;

    WorldPanel(ClientGameModel model) {
        this.model = model;
        fgCanvas = new Canvas();
        bgCanvas = new Canvas();
        uiCanvas = new Canvas();
        fgGC = fgCanvas.getGraphicsContext2D();
        bgGC = bgCanvas.getGraphicsContext2D();

        this.getChildren().addAll(bgCanvas, fgCanvas, uiCanvas);

        fgCanvas.heightProperty().bind(this.heightProperty());
        fgCanvas.widthProperty().bind(this.widthProperty());
        bgCanvas.heightProperty().bind(this.heightProperty());
        bgCanvas.widthProperty().bind(this.widthProperty());
        uiCanvas.heightProperty().bind(this.heightProperty());
        uiCanvas.widthProperty().bind(this.widthProperty());

        uiCanvas.setFocusTraversable(true);

        uiCanvas.setOnMouseClicked(e -> {
            new Visitor.PlaceEntity().run(model.getLocalPlayer(), model);
        });

        uiCanvas.setOnMouseMoved(e -> {
            new Visitor.MoveEntity(e.getX(), e.getY(), uiCanvas.getWidth(), uiCanvas.getHeight()).run(model.getLocalPlayer(), model);
        });

        uiCanvas.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DIGIT1 || e.getCode() == KeyCode.NUMPAD1)
                new Visitor.ShowPlacement(0, 0).run(model.getLocalPlayer(), model);

            if (e.getCode() == KeyCode.DIGIT2 || e.getCode() == KeyCode.NUMPAD2)
                new Visitor.ShowPlacement(0, 0).run(model.getLocalPlayer(), model);
        });
    }

    double[] scaleFactor = {1};

    int tick[] = {0};

    public void draw() {
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
            // model.drawFG(fgGC, x, y, gameW, gameH);
            //

            bgGC.setFill(Color.LIGHTCYAN);
            bgGC.fillRect(0, 0, bgCanvas.getWidth(), bgCanvas.getHeight());
            fgGC.clearRect(-1111, -11111, 11111111, 1111111);

            model.drawBG(bgGC, GRID_X_SIZE / 2, GRID_Y_SIZE / 2, GRID_X_SIZE, GRID_Y_SIZE, tick[0] > WATER_ANIM_PERIOD / 2);
            model.drawFG(fgGC, GRID_X_SIZE / 2, GRID_Y_SIZE / 2, GRID_X_SIZE, GRID_Y_SIZE);
            DropShadow shadow = new DropShadow();
            shadow.setOffsetY(toDrawCoords(0.2));
            shadow.setColor(Color.color(0, 0, 0, .25));
            shadow.setSpread(0.7);

            fgGC.applyEffect(shadow);
            tick[0] = (tick[0] + 1) % WATER_ANIM_PERIOD;
        }
    }
}
