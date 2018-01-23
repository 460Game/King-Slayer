package game.model.Game.WorldObject.Entity;

import game.model.Game.Map.Tile;
import game.model.Game.Model.GameModel;
import game.model.Game.WorldObject.Shape.CellShape;
import game.model.Game.WorldObject.Shape.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.IOException;

import static Util.Const.TILE_PIXELS;

public class Stone extends Resource {
    @Override
    public void collision(GameModel model, Entity collidesWith) {

    }

    //    Shape shape = new CircleShape(0,0,0.5);
    Shape shape = new CellShape(); // TODO FIX THis

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void update(long time, GameModel model) {

    }

    static Image[] image = new Image[8];
    private int imageNum = 0;

    static {
        try {
            image[0] = new Image(Tile.class.getResource("boulder_tall.png").openStream());
            image[1] = new Image(Tile.class.getResource("boulder_1.png").openStream());
            image[2] = new Image(Tile.class.getResource("boulder_2.png").openStream());
            image[3] = new Image(Tile.class.getResource("boulder_3.png").openStream());
            image[4] = new Image(Tile.class.getResource("boulder_4.png").openStream());
            image[5] = new Image(Tile.class.getResource("boulder_5.png").openStream());
            image[6] = new Image(Tile.class.getResource("boulder_6.png").openStream());
            image[7] = new Image(Tile.class.getResource("boulder_7.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        draw(gc, image[imageNum]);
    }

    @Override
    public double getDrawZ() {
        return getY();
    }
}
