import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
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

    Rectangle rect1;

    public Grass(int x, int y, Model model) {
        super(x, y, model);
        Rectangle rect1 = new Rectangle(x, y, 1, 1);
        rect1.setFill(Color.GREEN);
    }

    @Override
    public void drawInit(ObservableList<Node> g) {
        g.add(rect1);
    }

    @Override
    public void drawUpdate() {

    }
}
