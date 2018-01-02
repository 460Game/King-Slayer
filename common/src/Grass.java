import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/*
demo tile
 */
public class Grass extends Tile {

    @Override
    public boolean isPassable() {
        return true;
    }

    double x,y,w,h;

    public Grass(int x, int y, Model model) {
        super(x, y, model);
        x = 1;
        y = 1;
        w = 10;
        h = 10;
    }

    @Override
    public void draw(GraphicsContext gc, double x, double y) {
        gc.fillRect(x, y, w, h);
    }
}
