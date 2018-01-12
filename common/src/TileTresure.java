import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileTresure extends TilePassable {

    public TileTresure(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.GOLD;
    }
}
