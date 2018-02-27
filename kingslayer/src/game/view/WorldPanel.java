package game.view;

import com.esotericsoftware.minlog.Log;
import game.model.game.model.ClientGameModel;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Region;
import javafx.scene.transform.Affine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
    private Canvas uiCanvas;
    private GraphicsContext fgGC;
    private GraphicsContext bgGC;
    public GraphicsContext uiGC;

    double mouseX = 0;
    double mouseY = 0;

    WorldPanel(ClientGameModel model) {
        this.model = model;
        fgCanvas = new Canvas();
        bgCanvas = new Canvas();
        uiCanvas = new Canvas();
        fgGC = fgCanvas.getGraphicsContext2D();
        bgGC = bgCanvas.getGraphicsContext2D();
        uiGC = uiCanvas.getGraphicsContext2D();

        this.getChildren().addAll(bgCanvas, fgCanvas, uiCanvas);

        fgCanvas.heightProperty().bind(this.heightProperty());
        fgCanvas.widthProperty().bind(this.widthProperty());
        bgCanvas.heightProperty().bind(this.heightProperty());
        bgCanvas.widthProperty().bind(this.widthProperty());
        uiCanvas.heightProperty().bind(this.heightProperty());
        uiCanvas.widthProperty().bind(this.widthProperty());

        uiCanvas.setFocusTraversable(true);

        BGImage1 = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        BGImage2 = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
        model.writeBackground(BGImage1, true);
        model.writeBackground(BGImage2, false);

    }

    private double[] scaleFactor = {2};
    private int waterTick[] = {0};

    private double xt;
    private double yt;

    private double x;
    private double y;

    public double getCamX() {
        return x;
    }

    public double getCamY() {
        return y;
    }

    public double getCamW() {
        return gameW;
    }

    public double getCamH() {
        return gameH;
    }

    private double gameW;
    private double gameH;

    public void draw() {
        x = model.getLocalPlayer().getX() + 0.1 * toWorldCoords(mouseX - getWidth()/2);
        y = model.getLocalPlayer().getY() + 0.15 * toWorldCoords(mouseY - getHeight()/2);
        x = Math.min(Math.max(gameW/2, x), model.getMapWidth() - gameW/2);
        y = Math.min(Math.max(gameH/2, y), model.getMapHeight() - gameH/2);
        gameW = toWorldCoords(getWidth() / scaleFactor[0]);
        gameH = toWorldCoords(getHeight() / scaleFactor[0]);
        xt = -toDrawCoords(x * scaleFactor[0]) + getWidth() / 2;
        yt = -toDrawCoords(y * scaleFactor[0]) + getHeight() / 2;
        fgGC.setTransform(new Affine());
        bgGC.setTransform(new Affine());
        fgGC.translate(xt, yt);
        bgGC.translate(xt, yt);
        fgGC.transform(new Affine(Affine.scale(scaleFactor[0], scaleFactor[0])));
        bgGC.transform(new Affine(Affine.scale(scaleFactor[0], scaleFactor[0])));

        fgGC.clearRect(-1111, -11111, 11111111, 1111111);
        bgGC.drawImage(waterTick[0] > WATER_ANIM_PERIOD / 2 ? BGImage1 : BGImage2, 0, 0);
        model.drawForeground(fgGC, GRID_X_SIZE / 2, GRID_Y_SIZE / 2, gameW, gameH);
        waterTick[0] = (waterTick[0] + 1) % WATER_ANIM_PERIOD;


        this.setOnScroll(e -> {
            if(e.getDeltaY() > 0)
                scaleFactor[0] *= 1.111111111111111;
            else
                scaleFactor[0] *= 0.9;
        });

        this.setOnKeyPressed(e -> {
            if (currentlyPressed.contains(e.getCode()))
                return;
            currentlyPressed.add(e.getCode());
            if(keyPressAction.containsKey(e.getCode()))
                keyPressAction.get(e.getCode()).run();
            if(keyPressConsumer != null)
                keyPressConsumer.accept(e.getCode());
        });

        this.setOnKeyReleased(e -> {
            currentlyPressed.remove(e.getCode());
            if(keyReleaseAction.containsKey(e.getCode()))
                keyReleaseAction.get(e.getCode()).run();
            if(keyReleaseConsumer != null)
                keyReleaseConsumer.accept(e.getCode());
        });


        this.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.PRIMARY && leftClick != null)
                leftClick.accept(screenToGameX(e.getX()), screenToGameY(e.getY()));
            if(e.getButton() == MouseButton.SECONDARY && rightClick != null)
                rightClick.accept(screenToGameX(e.getX()), screenToGameY(e.getY()));
        });

    }

    public double screenToGameX(double x) {
        return (x- xt)/TILE_PIXELS / scaleFactor[0] ;
    }

    public double screenToGameY(double y) {
        return (y - yt) / TILE_PIXELS / scaleFactor[0];
    }

    private Set<KeyCode> currentlyPressed = new TreeSet<>();

    private BiConsumer<Double, Double> leftClick = null;
    private BiConsumer<Double, Double> rightClick = null;

    void onGameLeftClick(BiConsumer<Double, Double> consumer) {
        leftClick = consumer;
    }

    void onGameRightClick(BiConsumer<Double, Double> consumer) {
        rightClick = consumer;
    }

    private Map<KeyCode, Runnable> keyPressAction = new HashMap<>();
    private Consumer<KeyCode> keyPressConsumer = null;
    private Map<KeyCode, Runnable> keyReleaseAction = new HashMap<>();
    private Consumer<KeyCode> keyReleaseConsumer = null;

    void onKeyPress(KeyCode keyCode, Runnable action) {
        keyPressAction.put(keyCode, action);
    }

    void onKeyPress(Runnable action, KeyCode... keyCodes) {
        for (KeyCode keyCode : keyCodes) {
            keyPressAction.put(keyCode, action);
        }
    }
    void onKeyRelease(KeyCode keyCode, Runnable action) {
        keyReleaseAction.put(keyCode, action);
    }

    void onKeyRelease(Runnable action, KeyCode... keyCodes) {
        for (KeyCode keyCode : keyCodes) {
            keyReleaseAction.put(keyCode, action);
        }
    }

    void onKeyPress(Consumer<KeyCode> consumer) {
        keyPressConsumer = consumer;
    }

    void onKeyRelease(Consumer<KeyCode> consumer) {
        keyReleaseConsumer = consumer;
    }

    void onGameKeyUp(Consumer<KeyCode> consumer) {
        this.setOnKeyPressed(e -> {
            consumer.accept(e.getCode());
        });
    }

    public void onGameMouseMove(BiConsumer<Double, Double> consumer) {
        this.setOnMouseMoved(e ->{
            mouseX = e.getX();
            mouseY = e.getY();
            consumer.accept(screenToGameX(e.getX()), screenToGameY(e.getY()));
        });
    }
}
