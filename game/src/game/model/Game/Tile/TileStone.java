package game.model.Game.Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class TileStone extends TileTest {

    static Image grassImage;

    static {
        try {
            grassImage = new Image(TileGrass.class.getResource("rock.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, int x, int y) {
        gc.drawImage(grassImage, x*64, y*64, 64, 64);
    }

    @Override
    public Paint getColor() {
        return Color.BLUEVIOLET;
    }
}
