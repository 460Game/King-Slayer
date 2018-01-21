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

public class Tree extends Resource {
    @Override
    public void collision(GameModel model, Entity collidesWith) {

    }

//    Shape shape = new CircleShape(0,0,0);
    Shape shape = new CellShape(); // TODO FIX THis

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void update(long time, GameModel model) {

    }

    static Image image;

    static {
        try {
            image = new Image(Tile.class.getResource("tree.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(this.image, this.getX() * TILE_PIXELS - TILE_PIXELS/2, this.getY() * TILE_PIXELS - TILE_PIXELS/2, TILE_PIXELS, 1.5*TILE_PIXELS);
    }

    @Override
    public double getDrawZ() {
        return getY();
    }
}
