import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileWall extends TileTest {

    public TileWall(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.BLACK;
    }
}
