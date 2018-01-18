package game.model.Game.Tile;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class TileTree extends TileTest {

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public Paint getColor() {
        return Color.GREEN;
    }

    static Image treeImage;

    static {
        try {
            treeImage = new Image(TileTree.class.getResource("tree.png").openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(GraphicsContext gc, int x, int y) {
        gc.drawImage(treeImage, x*64, y*64 - 40, 64, 104);
    }
}
