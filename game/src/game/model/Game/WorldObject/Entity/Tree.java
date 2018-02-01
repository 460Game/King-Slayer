package game.model.Game.WorldObject.Entity;

import game.model.Game.Model.GameModel;
import game.model.Game.Map.Tile;
import game.model.Game.WorldObject.Shape.CellShape;
import game.model.Game.WorldObject.Shape.CircleShape;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

import static Util.Const.TILE_PIXELS;

/**
 * The basic resource in the game world. It produces wood
 * and can be found all over the map.
 */
public class Tree extends Resource {

    /**
     * The shape of the wood resource.
     */
//    Shape shape = new CircleShape(0,0,0.5);
    Shape shape = new CellShape(); // TODO FIX THis

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void update(long time, GameModel model) {

    }

    /**
     * Holds all the images that represent the amount of wood
     * resource left in that tile.
     */
    static Image[] image = new Image[5];

    /**
     * The index of the image to be drawn.
     */
    private int imageNum = 0;

    // Get the various images for the resource. Each image corresponds to
    // a different amount of wood left.
    static {
        try {
            image[0] = new Image(Tile.class.getResource("tree.png").openStream());
            image[1] = new Image(Tile.class.getResource("tree_1.png").openStream());
            image[2] = new Image(Tile.class.getResource("tree_2.png").openStream());
            image[3] = new Image(Tile.class.getResource("tree_3.png").openStream());
            image[4] = new Image(Tile.class.getResource("tree_4.png").openStream());
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
