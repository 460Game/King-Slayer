import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TilePassable extends TileTest {

    public TilePassable(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.WHITE;
    }
}
