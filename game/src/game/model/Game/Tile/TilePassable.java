package game.model.Game.Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class TilePassable extends TileTest {

    @Override
    public Paint getColor() {
        return Color.WHITE;
    }


    static Image stoneImage;

    static {
        try {
            stoneImage = new Image(TileGrass.class.getResource("stone.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, int x, int y) {
        gc.drawImage(stoneImage, x*64, y*64, 64, 64);
    }
}
