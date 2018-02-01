package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.CellShape;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

import static Util.Const.TILE_PIXELS;

/**
 * The highest tier resource in the game world. It is the hardest
 * to find but are found in bunches.
 */
public class Metal extends Resource {

    /**
     * The shape of the metal resource.
     */
    //    private Shape shape = new CircleShape(0,0,0.5);
    private Shape shape = new CellShape(); // TODO FIX THis

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void update(long time, GameModel model) {

    }

    /**
     * Holds all the images that represent the amount of metal
     * resource left in that tile.
     */
    static Image[] image = new Image[11];

    /**
     * The index of the image to be drawn.
     */
    private int imageNum = 0;

    // Get the various images for the resource. Each image corresponds to
    // a different amount of metal left.
    static {
        try {
            image[0] = new Image(Tile.class.getResource("iron_ingots.png").openStream());
            image[1] = new Image(Tile.class.getResource("iron_ingots_1.png").openStream());
            image[2] = new Image(Tile.class.getResource("iron_ingots_2.png").openStream());
            image[3] = new Image(Tile.class.getResource("iron_ingots_3.png").openStream());
            image[4] = new Image(Tile.class.getResource("iron_ingots_4.png").openStream());
            image[5] = new Image(Tile.class.getResource("iron_ingots_5.png").openStream());
            image[6] = new Image(Tile.class.getResource("iron_ingots_6.png").openStream());
            image[7] = new Image(Tile.class.getResource("iron_ingots_7.png").openStream());
            image[8] = new Image(Tile.class.getResource("iron_ingots_8.png").openStream());
            image[9] = new Image(Tile.class.getResource("iron_ingots_9.png").openStream());
            image[10] = new Image(Tile.class.getResource("iron_ingots_10.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, GameModel model) {
        draw(gc, image[imageNum]);
    }

    @Override
    public double getDrawZ() {
        return getY();
    }
}
