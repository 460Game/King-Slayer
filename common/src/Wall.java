import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/*
demo tile
 */
public class Wall extends Tile {

    @Override
    public boolean isPassable() {
        return true;
    }

    double x,y,w,h;

    public Wall(int x, int y) {
        super(x, y);
        this.x = x*10;
        this.y = y*10;
        this.w = 10;
        this.h = 10;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLACK);
        gc.fillRect(x,y,w,h);
        gc.strokeRect(x,y,w,h);
    }
}
