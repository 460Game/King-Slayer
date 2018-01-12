import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;


/*
demo tile
 */
public class TileWater extends TileTest {

    @Override
    public boolean isPassable() {
        return false;
    }

    public TileWater(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.BLUE;
    }
}
