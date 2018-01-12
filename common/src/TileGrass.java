import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class TileGrass extends TileTest {

    public TileGrass(int x, int y) {
        super(x, y);
    }

    @Override
    public Paint getColor() {
        return Color.LIGHTGREEN;
    }
}
